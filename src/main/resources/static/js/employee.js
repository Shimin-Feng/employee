$(document).ready(function () {

	showSexAge();

});

// 姓名正则
const regExpEmployeeName = /^[\u4e00-\u9fa5\w\s•]{1,25}$/;
// 身份证正则
const regExpIdCard = /^\d{15}|\d{18}|(\d{17}X|x)$/;
// 住址正则
const regExpEmployeeAddress = /^[\u4e00-\u9fa5\w\s•,]{2,45}$/;
// 电话号码正则（暂时只验证在一般情况下的中国大陆移动手机号码）
const regExpEmployeePhoneNumber = /^1[3-9]\d{9}$/;

// Get token
function getToken() {
	return $("input:hidden[name='_csrf']").val();
}

// Show employees' sex and age
function showSexAge() {
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
}

window.onload = function () {

	// 添加员工
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
			// 因为开启了 csrf，所以增加请求头
			headers: {
				'X-CSRF-Token': getToken()
			},
			success: function (data, success, state) {
				if (state.status === 200 && state.readyState === 4) {
					$('#save').blur();
					$.ajax({
						type: 'GET',
						url: '/employee',
						// 因为开启了 csrf，所以增加请求头
						headers: {
							'X-CSRF-Token': getToken()
						},
						success: function (data, success, state) {
							if (state.status === 200 && state.readyState === 4) {
								updatePage(data);
							}
						},
						error: function () {
							alert('添加成功，但是添加后查询数据库时失败，请检查后重试。');
						}
					})
				}
			},
			error: function () {
				alert('添加失败，请检查后重试。');
			}
		})
	})

	const tbody = $('tbody');
	// 删除
	tbody.on('click', 'tr td .deleteEmployee', function (params) {
		if (params.currentTarget.id === '' || params.currentTarget.id === null) {
			alert('未获取到对应员工的 id，请检查后重试。');
			return false;
		}
		if (confirm('确定删除?')) {
			$.ajax({
				type: 'POST',
				// 最前面必须要加斜杠/ '/employee/deleteEmployee/'
				url: '/employee/deleteEmployee',
				data: {'employeeId': params.currentTarget.id},
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						params.currentTarget.onblur;
						const findInput = $('#findInput').val();
						const currentPage = $('.currentPage').text();
						if (findInput === '') {
							$.ajax({
								type: 'GET',
								url: '/employee?pageNum=' + (currentPage - 1),
								// 因为开启了 csrf，所以增加请求头
								headers: {
									'X-CSRF-Token': getToken()
								},
								success: function (data, success, state) {
									if (state.status === 200 && state.readyState === 4) {
										updatePage(data);
									}
								},
								error: function () {
									alert('删除成功，但是删除后查询数据库时失败，请检查后重试。');
								}
							})
						} else {
							$.ajax({
								type: 'POST',
								// 下面这行非常重要，没有会报错
								// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
								// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
								contentType: 'application/json',
								url: '/employee/findEmployee?pageNum=' + (currentPage - 1),
								data: JSON.stringify(new Employee($('#findSelect').val(), findInput)),
								// 因为开启了 csrf，所以增加请求头
								headers: {
									'X-CSRF-Token': getToken()
								},
								success: function (data, success, state) {
									if (state.status === 200 && state.readyState === 4) {
										if (/tfoot/.test(data)) {
											updatePage(data);
										}
									}
								},
								error: function () {
									alert('删除成功，但是删除后查询数据库时失败，请检查后重试。');
								}
							})
						}
					}
				},
				error: function () {
					alert('删除失败，请检查后重试。');
				}
			})
		}
	})

	// 更改
	tbody.on('click', 'tr td .updateEmployee', function (params) {
		// 获取当前 count
		const count = params.currentTarget.parentNode.parentNode.firstElementChild.textContent;
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
				this.employeeId = params.currentTarget.parentNode.nextElementSibling.firstElementChild.id;
				this.employeeName = employeeName;
				this.employeeSex = $('#trId' + count + ' td:eq(2) label select').val();
				this.employeeAge = $('#trId' + count + ' td:eq(3) label select').val();
				this.employeeIdCard = employeeIdCard;
				this.employeeAddress = employeeAddress;
				this.employeePhoneNumber = employeePhoneNumber;
				this.createdBy = $('#trId' + count + ' td:eq(7)').text();
				this.createdDate = Date.parse(params.currentTarget.parentNode.previousElementSibling.previousElementSibling.textContent);
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
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
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

	// 根据条件查找员工
	function findEmployee(param) {

		$.ajax({
			type: 'POST',
			// 下面这行非常重要，没有会报错
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
			contentType: 'application/json',
			url: '/employee/findEmployee',
			data: JSON.stringify(new Employee($('#findSelect').val(), param)),
			// 因为开启了 csrf，所以增加请求头
			// 终于成功了！！！
			headers: {
				// 不区分大小写
				'X-CSRF-Token': getToken()
			},
			// xml ===      直接 error
			// html ===     就是默认的整个页面的 html，没有 tbody 内容
			// script ===   也没有 tbody 内容
			// json ===     error
			// jsonp ===    error
			// text ===     整个页面的 html，没有 tbody 内容
			// !!!!!!!!!!!! 这是查找方法，不用重定向也可以把所有数据的第一页传回来 !!!!!!!!!!!!
			// xml ===      error
			// html ===     有 tbody 内容
			// script ===   有 tbody 内容
			// json ===     error
			// jsonp ===    error
			// text ===     有 tbody 内容
			// dataType: 'text',
			success: function (data, success, state) {
				if (state.status === 200 && state.readyState === 4) {
					if (/tfoot/.test(data)) {
						// 截取之后填充进页面
						const array1 = data.split('<tbody>');
						console.log(data);
						console.log(array1);
						console.log(array1[1]);
						const array2 = array1[1].split('</tbody>');
						console.log(array2);
						console.log(array2[1]);
						const array3 = array2[1].split('<div class="modal-footer no-margin-top">');
						console.log(array3);
						console.log(array3[1]);
						let array4 = array3[1].split('</div>');
						console.log(array4);
						console.log(array4[0]);
						// 把对象也写进页面请求路径中
						// encodeURIComponent(JSON.stringify(new Employee()))
						const ul = array4[0].replace(/\?/g, '/findEmployee?');
						// 截取字符串之后不需要将其转换为 HTML
						// $('tbody').html($(array2[0]));
						$('tbody').html(array2[0]);
						$('.no-margin-top').html(ul);
						showSexAge();
					} else {
						$('tbody tr').remove();
						$('tfoot').remove();
						$('tbody').html('<tr><td colspan="12">无匹配结果</td></tr>');
					}
				}
			},
			error: function () {
				alert('查找失败，请检查后重试。');
			}
		})

	}

	const tfoot = $('tfoot');
	// 分页-首页
	tfoot.on('click', 'tr td div ul li #frontPage', function () {
		const findInput = $('#findInput').val();
		if (findInput === '') {
			$.ajax({
				type: 'POST',
				url: '/employee?pageNum=0',
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						updatePage(data);
					}
				},
				error: function () {
					alert('翻页失败，请检查后重试。');
				}
			})
		} else {
			$.ajax({
				type: 'POST',
				// 下面这行非常重要，没有会报错
				// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
				// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
				contentType: 'application/json',
				url: '/employee/findEmployee?pageNum=0',
				data: JSON.stringify(new Employee($('#findSelect').val(), findInput)),
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						updatePage(data);
					}
				},
				error: function () {
					alert('翻页失败，请检查后重试。');
				}
			})
		}
	})
	// 分页-上一页
	tfoot.on('click', 'tr td div ul li #previousPageable', function (params) {
		const findInput = $('#findInput').val();
		if (findInput === '') {
			$.ajax({
				type: 'POST',
				url: '/employee?pageNum=' + params.currentTarget.className,
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						updatePage(data);
					}
				},
				error: function () {
					alert('翻页失败，请检查后重试。');
				}
			})
		} else {
			$.ajax({
				type: 'POST',
				// 下面这行非常重要，没有会报错
				// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
				// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
				contentType: 'application/json',
				url: '/employee/findEmployee?pageNum=' + params.currentTarget.className,
				data: JSON.stringify(new Employee($('#findSelect').val(), findInput)),
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						updatePage(data);
					}
				},
				error: function () {
					alert('翻页失败，请检查后重试。');
				}
			})
		}
	})
	// 分页-中间页
	tfoot.on('click', 'tr td div ul li .midPages', function (params) {
		// console.log(params.currentTarget.textContent - 1);
		// return false;
		const findInput = $('#findInput').val();
		if (findInput === '') {
			$.ajax({
				type: 'POST',
				url: '/employee?pageNum=' + (params.currentTarget.textContent - 1),
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						updatePage(data);
					}
				},
				error: function () {
					alert('翻页失败，请检查后重试。');
				}
			})
		} else {
			$.ajax({
				type: 'POST',
				// 下面这行非常重要，没有会报错
				// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
				// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
				contentType: 'application/json',
				url: '/employee/findEmployee?pageNum=' + (params.currentTarget.textContent - 1),
				data: JSON.stringify(new Employee($('#findSelect').val(), findInput)),
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						updatePage(data);
					}
				},
				error: function () {
					alert('翻页失败，请检查后重试。');
				}
			})
		}
	})
	// 分页-下一页
	tfoot.on('click', 'tr td div ul li #nextPageable', function (params) {
		const findInput = $('#findInput').val();
		if (findInput === '') {
			$.ajax({
				type: 'POST',
				url: '/employee?pageNum=' + params.currentTarget.className,
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						updatePage(data);
					}
				},
				error: function () {
					alert('翻页失败，请检查后重试。');
				}
			})
		} else {
			$.ajax({
				type: 'POST',
				// 下面这行非常重要，没有会报错
				// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
				// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
				contentType: 'application/json',
				url: '/employee/findEmployee?pageNum=' + params.currentTarget.className,
				data: JSON.stringify(new Employee($('#findSelect').val(), findInput)),
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						updatePage(data);
					}
				},
				error: function () {
					alert('翻页失败，请检查后重试。');
				}
			})
		}
	})
	// 分页-尾页
	tfoot.on('click', 'tr td div ul li #lastPage', function (params) {
		const findInput = $('#findInput').val();
		if (findInput === '') {
			$.ajax({
				type: 'POST',
				url: '/employee?pageNum=' + params.currentTarget.className,
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						updatePage(data);
					}
				},
				error: function () {
					alert('翻页失败，请检查后重试。');
				}
			})
		} else {
			$.ajax({
				type: 'POST',
				// 下面这行非常重要，没有会报错
				// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
				// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
				contentType: 'application/json',
				url: '/employee/findEmployee?pageNum=' + params.currentTarget.className,
				data: JSON.stringify(new Employee($('#findSelect').val(), findInput)),
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						updatePage(data);
					}
				},
				error: function () {
					alert('翻页失败，请检查后重试。');
				}
			})
		}
	})

	// 更新页面数据
	function updatePage(data) {
		if (/tfoot/.test(data)) {
			// 截取之后填充进页面
			const array1 = data.split('<tbody>');
			console.log(data);
			console.log(array1);
			console.log(array1[1]);
			const array2 = array1[1].split('</tbody>');
			console.log(array2);
			console.log(array2[1]);
			const array3 = array2[1].split('<div class="modal-footer no-margin-top">');
			console.log(array3);
			console.log(array3[1]);
			let array4 = array3[1].split('</div>');
			console.log(array4);
			console.log(array4[0]);
			tbody.html(array2[0]);
			$('.no-margin-top').html(array4[0]);
			showSexAge();
		} else {
			$('tbody tr').remove();
			$('tfoot').remove();
			$('tbody').html('<tr><td colspan="12">无匹配结果</td></tr>');
		}
	}

	// 根据传参判断对象属性
	function Employee(attribute, param) {
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
}