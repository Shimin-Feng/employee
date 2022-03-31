// $(document).ready(function () {

$(function () {

// 手机号码验证
	jQuery.validator.addMethod("mobile", function (value, element) {
		const length = value.length
		const mobile = /^(((13\d)|(15\d))+\d{8})$/
		return this.optional(element) || (length === 11 && mobile.test(value));
	}, "手机号码格式错误");

// 电话号码验证
	jQuery.validator.addMethod("phone", function (value, element) {
		const tel = /^(0\d{2,3})?([2-9]\d{6,7})+(\d{1,4})?$/
		return this.optional(element) || (tel.test(value));
	}, "电话号码格式错误");

// 字母和数字的验证
	jQuery.validator.addMethod("charAndNum", function (value, element) {
		const chrnum = /^([a-zA-Z\d]+)$/
		return this.optional(element) || (chrnum.test(value));
	}, "只能输入数字和字母(字符A-Z, a-z, 0-9)");

// 中文的验证
	jQuery.validator.addMethod("chinese", function (value, element) {
		const chinese = /^[\u4e00-\u9fa5]+$/
		return this.optional(element) || (chinese.test(value));
	}, "只能输入中文");

// 下拉框验证
	$.validator.addMethod("selectNone", function (value, element, param) {
		return value !== param[0];
	}, "必须选择一项");

//整数位，小数位验证
	jQuery.validator.addMethod("decimal", function (value, element, param) {
		return this.optional(element) || new RegExp("^-?\\d{1," + (param[0] != null ? param[0] : "") + "}" + (param[1] != null ? (param[1] > 0 ? "(\\.\\d{1," + param[1] + "})?$" : "$") : "(\\.\\d+)?$")).test(value);
	}, $.validator.format("内容输入错误或者格式错误：整数位最多{0}位，小数位最多{1}位"));

// 验证值必须大于特定值(不能等于)
	jQuery.validator.addMethod("gt", function (value, element, param) {
		return value > param[0];
	}, $.validator.format("输入值必须大于 {0}!"));


	$("#employeeForm").validate({
		rules: {
			name: {required: true, rangelength: [1, 10], chinese: true},
			age: {required: true, decimal: [2, 2]},
			account: {required: true, charAndNum: true},
			mobile: {required: true, mobile: true},
			phone: {required: true, phone: true},
			code: {required: true, gt: [10]},
			type: {required: true, selectNone: [0]}

		}, messages: {
			name: {required: '请填写真实姓名', rangelength: '请输入1-10个字'},
			age: {required: '请填写手机号'},
			account: {required: '请填写账号'},
			mobile: {required: '请填写手机号'},
			phone: {required: '请填写电话号'},
			code: {required: '请填写身份证'},
			type: {required: '请选择类型'}
		}
	});
});
$("#myformSubmit").click(function () {
	//验证是否通过
	if (!$("#employeeForm").valid()) {
		// return;
	}
	//其他代码
});