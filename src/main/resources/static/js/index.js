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

	$('.employeeIdCard').blur(function () {
		console.log('根据时间自动调节背景！！！！！！！！！！！！！！！！！！！');
	})
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
function updateEmployee(employeeId, count) {
	// 验证姓名
	// ^[\u4E00-\u9FA5A-Za-z0-9_]+$  /\p{Unified_Ideograph}/u
	const regExpEmployeeName = /^[\u4e00-\u9fa5A-Za-z\s\\•]{1,25}$/;
	let employeeName = $('#trId' + count + ' td:eq(1) label input').val();
	if (!regExpEmployeeName.test(employeeName)) {
		alert('姓名只支持由 1 - 25 个汉字、英文、空格和•的组合。');
		return false;
	}

	// 验证身份证
	let employeeIdCard = $('#trId' + count + ' td:eq(4) label input').val();
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
		let lastNumber = calculateLastNumber(id17);
		if (lastNumber !== employeeIdCard.substring(17)) {
			alert('身份证号码格式有误，请检查后重试。');
			return false;
		}
	}

	// 验证电话号码（暂时就只验证：一般情况下的中国大陆移动手机号码）
	let employeePhoneNumber = $('#trId' + count + ' td:eq(6) label input').val();
	const regExpEmployeePhoneNumber = /^1[3-9]\d{9}$/;
	if (!regExpEmployeePhoneNumber.test(employeePhoneNumber)) {
		alert('电话号码格式有误，请检查后重试。');
		return false;
	}

	if (confirm('确定更改?')) {
		let lastModifiedDate = new Date().toJSON();
		lastModifiedDate = lastModifiedDate.substring(0, lastModifiedDate.length - 5);

		function Employee() {
			this.employeeId = employeeId;
			this.employeeName = employeeName;
			this.employeeSex = $('#trId' + count + ' td:eq(2) label select').val();
			this.employeeAge = $('#trId' + count + ' td:eq(3) label select').val();
			this.employeeIdCard = employeeIdCard;
			this.employeeAddress = $('#trId' + count + ' td:eq(5) label input').val();
			this.employeePhoneNumber = employeePhoneNumber;
			this.createdBy = $('#trId' + count + ' td:eq(7)').text();
			this.createdDate = $('#trId' + count + ' td:eq(8)').text();
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
				$('#trId' + count + ' td:eq(9)').html(lastModifiedDate);
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

// $(function () {
// 	// 手机号码验证
// 	// $.validator.addMethod("mobile", function (value, element) {
// 	// 	const length = value.length
// 	// 	const mobile = /^(((13\d)|(15\d))+\d{8})$/
// 	// 	return this.optional(element) || (length === 11 && mobile.test(value));
// 	// }, "手机号码格式错误");
// 	//
// 	// // 电话号码验证
// 	// $.validator.addMethod("phone", function (value, element) {
// 	// 	const tel = /^(0\d{2,3})?([2-9]\d{6,7})+(\d{1,4})?$/
// 	// 	return this.optional(element) || (tel.test(value));
// 	// }, "电话号码格式错误");
// 	//
// 	// // 字母和数字的验证
// 	// $.validator.addMethod("charAndNum", function (value, element) {
// 	// 	const chrnum = /^([a-zA-Z\d]+)$/
// 	// 	return this.optional(element) || (chrnum.test(value));
// 	// }, "只能输入数字和字母(字符A-Z, a-z, 0-9)");
//
// 	// 中文的验证
// 	$.validator.addMethod("chinese", function (value, element) {
// 		const chinese = /^[\u4e00-\u9fa5]{1,25}}$/
// 		return this.optional(element) || (chinese.test(value));
// 	}, "只能输入中文");
//
// 	// // 下拉框验证
// 	// $.validator.addMethod("selectNone", function (value, element, param) {
// 	// 	return value !== param[0];
// 	// }, "必须选择一项");
// 	//
// 	// //整数位，小数位验证
// 	// $.validator.addMethod("decimal", function (value, element, param) {
// 	// 	return this.optional(element) || new RegExp("^-?\\d{1," + (param[0] != null ? param[0] : "") + "}" + (param[1] != null ? (param[1] > 0 ? "(\\.\\d{1," + param[1] + "})?$" : "$") : "(\\.\\d+)?$")).test(value);
// 	// }, $.validator.format("内容输入错误或者格式错误：整数位最多{0}位，小数位最多{1}位"));
// 	//
// 	// // 验证值必须大于特定值(不能等于)
// 	// $.validator.addMethod("gt", function (value, element, param) {
// 	// 	return value > param[0];
// 	// }, $.validator.format("输入值必须大于 {0}!"));
//
// 	$('#employeeForm').validate({
// 		rules: {
// 			employeeName: {required: true, rangelength: [1, 10], chinese: true},
// 			// age: {required: true, decimal: [2, 2]},
// 			// account: {required: true, charAndNum: true},
// 			// mobile: {required: true, mobile: true},
// 			// phone: {required: true, phone: true},
// 			// code: {required: true, gt: [10]},
// 			// type: {required: true, selectNone: [0]}
// 		}, messages: {
// 			employeeName: {required: '请填写真实姓名', rangelength: '请输入1-10个字'},
// 			// age: {required: '请填写手机号'},
// 			// account: {required: '请填写账号'},
// 			// mobile: {required: '请填写手机号'},
// 			// phone: {required: '请填写电话号'},
// 			// code: {required: '请填写身份证'},
// 			// type: {required: '请选择类型'}
// 		}
// 	});
// });
//
// $('#employeeForm').click(function () {
// 	//验证是否通过
// 	if (!$("#employeeForm").valid()) {
// 		alert('aaaaaaaaaaaaaaaaaaaa');
// 		return;
// 	}
// 	alert('bbbbbbbbbbbbbbbbbbbbbbb');
// });