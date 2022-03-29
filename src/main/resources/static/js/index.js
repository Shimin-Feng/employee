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

	$('.idCard').blur(function () {
		console.log('根据时间自动调节背景！！！！！！！！！！！！！！！！！！！');
	})
});

// 确定删除?
function deleteEmployee(id, input) {
	if (confirm('确定删除?')) {
		$.ajax({
			type: 'DELETE',
			// 最前面必须要加斜杠/ '/employee/deleteById/'
			url: '/employee/deleteById/' + id,
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
function updateEmployee(id, count) {
	let id17 = [];
	let idCard = $('#trId' + count + ' td:eq(4) label input').val();
	for (let i = 0, j = idCard.length - 1; i < idCard.length - 1, j > 0; i++, j--) {
		id17[i] = idCard.substring(i, idCard.length - j);
	}
	let lastNumber = calculateLastNumber(id17);
	const regIdNo = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
	if (!regIdNo.test(idCard) || lastNumber !== idCard.substring(17)) {
		alert('身份证号码填写有误。');
		return false;
	}
	if (confirm('确定更改?')) {
		let date = new Date().toJSON();
		date = date.substring(0, date.length - 5);

		function Employee() {
			this.id = id;
			this.name = $('#trId' + count + ' td:eq(1) label input').val();
			this.sex = $('#trId' + count + ' td:eq(2) label select').val();
			this.age = $('#trId' + count + ' td:eq(3) label select').val();
			this.idCard = idCard;
			this.address = $('#trId' + count + ' td:eq(5) label input').val();
			this.phoneNumber = $('#trId' + count + ' td:eq(6) label input').val();
			this.createdBy = $('#trId' + count + ' td:eq(7)').text();
			this.createdDate = $('#trId' + count + ' td:eq(8)').text();
			this.lastModifiedDate = date;
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
				$('#trId' + count + ' td:eq(9)').html(date);
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