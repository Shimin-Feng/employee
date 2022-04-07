$(document).ready(function () {

	// Show employees' sex and age
	// Get the total number of employees
	const totalNumber = $('tbody tr').length;
	for (let i = 1; i <= totalNumber; i++) {
		// every employee's sex
		let sex = $('#sexSelectId' + i).attr('class');
		// every employee's age
		let age = $('#ageSelectId' + i).attr('class');
		// 根据性别使 option 选中
		$('#sexSelectId' + i + " option[value='" + sex + "']").attr('selected', 'selected');
		// 根据年龄使 option 选中
		$('#ageSelectId' + i + " option[value='" + age + "']").attr('selected', 'selected');
	}

	// 添加员工
	// function (params)
	// html 如何给 params 传参？
	$('#saveEmployee').on('click', function () {

		// 验证姓名
		// 已取消输入时限制（以下代码），因为体验并不好
		const employeeName = $('#recipient-name').val();
		if (!regExpEmployeeName.test(employeeName)) {
			alert('姓名只支持由 1 - 25 个汉字、英文、数字、空格和•的组合。');
			return false;
		}

		// 验证身份证
		let employeeIdCard = $('#recipient-idCard').val();
		if (employeeIdCard.length !== 15 && employeeIdCard.length !== 18) {
			alert('请填写 15 或者 18 位身份证号码。');
			return false;
		}
		// 15、18 位身份证号码第一重验证
		if (!regExpIdCard.test(employeeIdCard)) {
			alert('身份证号码格式有误，请检查后重试。');
			return false;
		}
		// 18 位身份证号码第二重验证
		if (employeeIdCard.length === 18) {
			let id17 = [];
			for (let i = 0, j = 17; i < 17, j > 0; i++, j--) {
				id17[i] = employeeIdCard.substring(i, 18 - j);
			}
			const lastNumber = calculateLastNumber(id17);
			employeeIdCard = employeeIdCard.toUpperCase();
			if (lastNumber !== employeeIdCard.substring(17)) {
				alert('身份证号码格式有误，请检查后重试。');
				return false;
			}
		}

		// 验证住址
		const employeeAddress = $('#recipient-address').val();
		if (!regExpEmployeeAddress.test(employeeAddress)) {
			alert('住址只支持由最多 45 个汉字、英文、空格、英文逗号和•的组合。');
			return false;
		}

		// 验证电话号码（暂时只验证在一般情况下的中国大陆移动手机号码）
		const employeePhoneNumber = $('#recipient-phoneNumber').val();
		if (!regExpEmployeePhoneNumber.test(employeePhoneNumber)) {
			alert('电话号码格式有误，请检查后重试。');
			return false;
		}

		const createdDate = new Date();

		function Employee() {
			this.employeeName = employeeName;
			this.employeeSex = $('#recipient-sxe').val();
			this.employeeAge = $('#recipient-age').val();
			this.employeeIdCard = employeeIdCard;
			this.employeeAddress = employeeAddress;
			this.employeePhoneNumber = employeePhoneNumber;
			this.createdBy = $('#username').text();
			this.createdDate = createdDate;
			this.lastModifiedDate = createdDate;
		}

		$.ajax({
			type: 'POST',
			// 下面这行非常重要，没有会报错
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
			contentType: 'application/json',
			url: '/employee/saveEmployee',
			data: JSON.stringify(new Employee()),
			success: function (data, success, state) {
				if (state.status === 200 && state.readyState === 4) {
					$('#save').blur();
					// test...
					console.log(data.valueOf());
					// undefined
					console.log(data.data);
					// console.log(data.success());
					console.log(data.toString());
					console.log(data.toLocaleString());
					// console.log(data.error());
					// undefined
					console.log(data.url);
					// undefined
					console.log(data.type);
					// undefined
					console.log(data.createdBy);
					console.log(data.constructor);
					console.log(success);
					console.log(state);
					console.log(state.readyState);
					console.log(state.responseText);
					console.log(state.status);
					console.log(state.statusText);
					console.log(state.state());
					console.log(state.data);
					console.log(state.constructor);
					console.log(state.contentType);
					console.log(state.employeeName);
					console.log(state.type);
					console.log(state.url);
					console.log(state.hasOwnProperty(state));
					console.log(state.isPrototypeOf(state));
					console.log(state.toLocaleString());
					console.log(state.propertyIsEnumerable(state));
					console.log(state.toString());
					console.log(state.valueOf());
				}
			},
			error: function () {
				alert('添加失败，请检查后重试。');
			}
		})
	})

	// 查找员工
	// 尝试过把下面两个方法写进一个方法里，但不理想
	// 点击事件
	$('#findButton').on('click', function () {
		const param = $('#findInput').val();
		if (param !== '') {
			findEmployee(param);
			$('#findButton').blur();
		}
	})
	// 输入完搜索内容后的回车事件
	$('#findInput').on('keydown', function (e) {
		const findInput = $('#findInput');
		const param = findInput.val();
		if (param !== '') {
			const eCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
			if (eCode === 13) {
				findEmployee(param);
				findInput.blur();
			}
		}
	})

});

// 姓名正则
const regExpEmployeeName = /^[\u4e00-\u9fa5\w\s•]{1,25}$/;
// 身份证正则
const regExpIdCard = /^\d{15}|\d{18}|(\d{17}X|x)$/;
// 住址正则
const regExpEmployeeAddress = /^[\u4e00-\u9fa5\w\s•,]{2,45}$/;
// 电话号码正则（暂时只验证在一般情况下的中国大陆移动手机号码）
const regExpEmployeePhoneNumber = /^1[3-9]\d{9}$/;

// 根据条件查找员工
function findEmployee(param) {

	const attribute = $('#findSelect').val();

	function Employee() {
		if (attribute === 'employeeName') {
			this.employeeName = param;
		} else if (attribute === 'employeeSex') {
			this.employeeSex = param;
		} else if (attribute === 'employeeAge') {
			this.employeeAge = param;
		} else if (attribute === 'employeeIdCard') {
			this.employeeIdCard = param;
		} else if (attribute === 'employeeAddress') {
			this.employeeAddress = param;
		} else if (attribute === 'employeePhoneNumber') {
			this.employeePhoneNumber = param;
		} else if (attribute === 'createdBy') {
			this.createdBy = param;
		} else if (attribute === 'createdDate') {
			this.createdDate = param;
		} else if (attribute === 'lastModifiedDate') {
			this.lastModifiedDate = param;
		}
	}

	$.ajax({
		type: 'POST',
		// 下面这行非常重要，没有会报错
		// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
		// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
		contentType: 'application/json',
		url: '/employee/findEmployee',
		data: JSON.stringify(new Employee()),
		// xml ===      直接 error
		// html ===     就是默认的整个页面的 html，没有 tbody 内容
		// script ===   也没有 tbody 内容
		// json ===     error
		// jsonp ===    error
		// text ===     整个页面的 html，没有 tbody 内容
		// !!!!!!!!!!!!!!! 这是查找方法，不用重定向也可以把所有数据的第一页传回来 !!!!!!!!!!!!!!!
		// xml ===      error
		// html ===     有 tbody 内容
		// script ===   有 tbody 内容
		// json ===     error
		// jsonp ===    error
		// text ===     有 tbody 内容
		// dataType: 'text',
		success: function (data, success, state) {
			if (state.status === 200 && state.readyState === 4) {
				// 截取之后填充进页面
				const array1 = data.split('<tbody>');
				const array2 = array1[1].split('</tbody>');
				const array3 = array2[1].split('<div class="modal-footer no-margin-top">');
				let array4 = array3[1].split('</div>', 1181);
				const regExp = /index\?/g;
				// 把对象也写进页面请求路径中
				// encodeURIComponent(JSON.stringify(new Employee()))
				array4 = array4[0].replace(regExp, 'findEmployee?' + encodeURIComponent(JSON.stringify(new Employee())) + '&');
				// 截取字符串之后不需要将其转换为 HTML
				// $('tbody').html($(array2[0]));
				$('tbody').html(array2[0]);
				$('.no-margin-top').html(array4);
			}
		},
		error: function () {
			alert('查询失败，请检查字段后重试。');
		}
	})

}

// 确定删除?
function deleteEmployee(employeeId, input) {
	if (confirm('确定删除?')) {
		$.ajax({
			type: 'POST',
			// 最前面必须要加斜杠/ '/employee/deleteEmployee/'
			url: '/employee/deleteEmployee',
			data: {'employeeId': employeeId},
			success: function (data, success, state) {
				if (state.status === 200 && state.readyState === 4) {
					const tr = input.parentNode.parentNode;
					const tbody = tr.parentNode;
					tbody.removeChild(tr);
					// Get the total number of employees
					const totalNumber = $('tbody tr').length;
					for (let i = 0; i < totalNumber; i++) {
						$('tbody tr:eq(' + i + ') td:eq(0)').html(i + 1);
					}
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
	const employeeAddress = $('#trId' + count + ' td:eq(5) label input').val();
	if (!regExpEmployeeAddress.test(employeeAddress)) {
		alert('住址只支持由最多 45 个汉字、英文、空格和•的组合。');
		return false;
	}
	// 验证电话号码（暂时只验证在一般情况下的中国大陆移动手机号码）
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
			success: function (data, success, state) {
				if (state.status === 200 && state.readyState === 4) {
					$('#trId' + count + ' td:eq(9)').html(year + '-' + month + '-' + date + ' ' + hours + ':' + minutes + ':' + seconds);
				}
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