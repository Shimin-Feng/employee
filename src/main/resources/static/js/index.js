// Show employees' sex and age
$(document).ready(function () {
	// Get the total number of employees
	const totalNumber = $('tbody tr').length;
	for (let i = 1; i <= totalNumber; i++) {
		// every employee's sex
		let sex = $('#sexSelectId' + i).attr("class");
		// every employee's age
		let age = $('#ageSelectId' + i).attr("class");
		// 根据性别使 option 选中
		$('#sexSelectId' + i + " option[value='" + sex + "']").attr('selected', 'selected');
		// 根据年龄使 option 选中
		$('#ageSelectId' + i + " option[value='" + age + "']").attr('selected', 'selected');
	}
});

// 确定删除?
function deleteEmployee(employeeId, input) {
	if (confirm('确定删除?')) {
		$.ajax({
			type: 'DELETE',
			// 最前面必须要加斜杠/ '/employee/deleteEmployee/'
			url: '/employee/deleteEmployee/' + employeeId,
			success: function () {
				const tr = input.parentNode.parentNode.parentNode;
				const tbody = tr.parentNode;
				tbody.removeChild(tr);
				// Get the total number of employees
				const totalNumber = $('tbody tr').length;
				for (let i = 0; i < totalNumber; i++) {
					$('tbody tr:eq(' + i + ') td:eq(0)').html(i + 1);
				}
			},
			error: function () {
				alert('删除失败，请检查后重试。');
			}
		})
	}
}

// 确定更改?
function updateEmployee(count, employeeId, createdDate) {
	// 验证姓名
	// 已取消输入时限制（以下代码），因为体验并不好
	// th:oninput="value=value.replace(/[^\u4e00-\u9fa5\w\s•]/,'')"
	const regExpEmployeeName = /^[\u4e00-\u9fa5\w\s•]{1,25}$/;
	const employeeName = $('#trId' + count + ' td:eq(1) label input').val();
	if (!regExpEmployeeName.test(employeeName)) {
		alert('姓名只支持由 1 - 25 个汉字、英文、数字、空格和•的组合。');
		return false;
	}

	// 验证身份证
	const employeeIdCard = $('#trId' + count + ' td:eq(4) label input').val();
	if (employeeIdCard.length !== 15 && employeeIdCard.length !== 18) {
		alert('身份证号码格式有误，请检查后重试。');
		return false;
	}
	const regExpIdCard = /^\d{15}|\d{18}|(\d{17}X|x)$/;
	// 15 位身份证
	if (!regExpIdCard.test(employeeIdCard)) {
		alert('身份证号码格式有误，请检查后重试。');
		return false;
	}
	// 18 位身份证
	if (employeeIdCard.length === 18) {
		let id17 = [];
		for (let i = 0, j = employeeIdCard.length - 1; i < employeeIdCard.length - 1, j > 0; i++, j--) {
			id17[i] = employeeIdCard.substring(i, employeeIdCard.length - j);
		}
		const lastNumber = calculateLastNumber(id17);
		if (lastNumber !== employeeIdCard.substring(17)) {
			alert('身份证号码格式有误，请检查后重试。');
			return false;
		}
	}

	// 验证住址
	const regExpEmployeeAddress = /^[\u4e00-\u9fa5\w\s•]{2,45}$/;
	const employeeAddress = $('#trId' + count + ' td:eq(5) label input').val();
	if (!regExpEmployeeAddress.test(employeeAddress)) {
		alert('住址只支持由最多 45 个汉字、英文、空格和•的组合。');
		return false;
	}

	// 验证电话号码（暂时只验证在一般情况下的中国大陆移动手机号码）
	const regExpEmployeePhoneNumber = /^1[3-9]\d{9}$/;
	const employeePhoneNumber = $('#trId' + count + ' td:eq(6) label input').val();
	if (!regExpEmployeePhoneNumber.test(employeePhoneNumber)) {
		alert('电话号码格式有误，请检查后重试。');
		return false;
	}

	if (confirm('确定更改?')) {
		const lastModifiedDate = new Date();
		const year = lastModifiedDate.getFullYear();
		const month = lastModifiedDate.getMonth() + 1 < 10 ? '0' + (lastModifiedDate.getMonth() + 1) : lastModifiedDate.getMonth() + 1;
		const date = lastModifiedDate.getDate() < 10 ? '0' + lastModifiedDate.getDate() : lastModifiedDate.getDate();
		const hours = lastModifiedDate.getHours() < 10 ? '0' + lastModifiedDate.getHours() : lastModifiedDate.getHours();
		const minutes = lastModifiedDate.getMinutes() < 10 ? '0' + lastModifiedDate.getMinutes() : lastModifiedDate.getMinutes();
		const seconds = lastModifiedDate.getSeconds() < 10 ? '0' + lastModifiedDate.getSeconds() : lastModifiedDate.getSeconds();

		function Employee() {
			this.employeeId = employeeId;
			this.employeeName = employeeName;
			this.employeeSex = $('#trId' + count + ' td:eq(2) label select').val();
			this.employeeAge = $('#trId' + count + ' td:eq(3) label select').val();
			this.employeeIdCard = employeeIdCard;
			this.employeeAddress = employeeAddress;
			this.employeePhoneNumber = employeePhoneNumber;
			this.createdBy = $('#trId' + count + ' td:eq(7)').text();
			this.createdDate = createdDate;
			this.lastModifiedDate = lastModifiedDate;
		}

		$.ajax({
			type: 'POST',
			// 下面这行非常重要，没有会报错
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
			contentType: 'application/json',
			url: '/employee/updateEmployee',
			data: JSON.stringify(new Employee()),
			success: function () {
				$('#trId' + count + ' td:eq(9)').html(year + '-' + month + '-' + date + ' ' + hours + ':' + minutes + ':' + seconds);
			},
			error: function () {
				alert('更改失败，请检查后重试。');
			}
		})
	}
}

// 根据所提供身份证的前 17 位算出最后一位
function calculateLastNumber(id17) {
	const weight = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
	const validate = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
	let sum = 0;
	let mode;
	for (let i = 0; i < id17.length; i++) {
		sum += id17[i] * weight[i];
	}
	mode = sum % 11;
	return validate[mode];
}

// 添加员工
function additionEmployee() {
	$('table').append('<tr><td>第二行</td></tr>');
}