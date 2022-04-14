// 姓名正则
const regExpEmployeeName = /^[\u4e00-\u9fa5\w\s•]{1,25}$/;
// 身份证正则
const regExpIdCard = /^\d{15}|\d{18}|(\d{17}X|x)$/;
// 住址正则
const regExpEmployeeAddress = /^[\u4e00-\u9fa5\w\s•,]{2,45}$/;
// 电话号码正则（暂时只验证在一般情况下的中国大陆移动手机号码）
const regExpEmployeePhoneNumber = /^1[3-9]\d{9}$/;

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

$(document).ready(function () {
	showSexAge();
});

window.onload = function () {

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

	// 格式化时间
	function getDateTime(dateTime) {
		const year = dateTime.getFullYear();
		const month = dateTime.getMonth() + 1 < 10 ? '0' + (dateTime.getMonth() + 1) : dateTime.getMonth() + 1;
		const date = dateTime.getDate() < 10 ? '0' + dateTime.getDate() : dateTime.getDate();
		const hours = dateTime.getHours() < 10 ? '0' + dateTime.getHours() : dateTime.getHours();
		const minutes = dateTime.getMinutes() < 10 ? '0' + dateTime.getMinutes() : dateTime.getMinutes();
		const seconds = dateTime.getSeconds() < 10 ? '0' + dateTime.getSeconds() : dateTime.getSeconds();
		return (year + '-' + month + '-' + date + ' ' + hours + ':' + minutes + ':' + seconds);
	}

	// 更新页面数据
	function updatePage(data) {
		if (/<option value="男">/.test(data)) {
			// 截取之后填充进页面
			const array1 = data.split('<tbody>');
			const array2 = array1[1].split('</tbody>');
			const array3 = array2[1].split('<div class="modal-footer no-margin-top">');
			let array4 = array3[1].split('</div>');
			$('tbody').html(array2[0]);
			$('.no-margin-top').html(array4[0]);
			showSexAge();
		} else {
			$('tbody tr').remove();
			$('tfoot').remove();
			$('tbody').html('<tr><td colspan="12">无匹配结果</td></tr>');
		}
	}

	// 根据传参判断对象属性
	function Employee(attribute, value) {
		if (attribute === 'employeeName') {
			this.employeeName = value;
		} else if (attribute === 'employeeSex') {
			this.employeeSex = value;
		} else if (attribute === 'employeeAge') {
			this.employeeAge = value;
		} else if (attribute === 'employeeIdCard') {
			this.employeeIdCard = value;
		} else if (attribute === 'employeeAddress') {
			this.employeeAddress = value;
		} else if (attribute === 'employeePhoneNumber') {
			this.employeePhoneNumber = value;
		} else if (attribute === 'createdBy') {
			this.createdBy = value;
		} else if (attribute === 'createdDate') {
			this.createdDate = value;
		} else if (attribute === 'lastModifiedDate') {
			this.lastModifiedDate = value;
		}
	}

	// Get token
	function getToken() {
		return $("input:hidden[name='_csrf']").val();
	}

	function regExp(employeeName, employeeIdCard, employeeAddress, employeePhoneNumber) {
		// 验证姓名
		// 已取消输入时限制（以下代码），因为体验并不好
		if (!regExpEmployeeName.test(employeeName)) {
			alert('姓名只支持由 1 - 25 个汉字、英文、数字、空格和•的组合。');
			return false;
		}

		// 验证身份证
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
		if (!regExpEmployeeAddress.test(employeeAddress)) {
			alert('住址只支持由最多 45 个汉字、英文、空格、英文逗号和•的组合。');
			return false;
		}

		// 验证电话号码（暂时只验证在一般情况下的中国大陆移动手机号码）
		if (!regExpEmployeePhoneNumber.test(employeePhoneNumber)) {
			alert('电话号码格式有误，请检查后重试。');
			return false;
		}
	}

	// 添加员工
	$('#saveEmployee').on('click', function () {

		$('#save').blur();
		if (regExp(
			$('#recipient-name').val(),
			$('#recipient-idCard').val(),
			$('#recipient-address').val(),
			$('#recipient-phoneNumber').val()
		) === false) {return false;}
		const createdDate = new Date();

		function Employee() {
			this.employeeName = $('#recipient-name').val();
			this.employeeSex = $('#recipient-sxe').val();
			this.employeeAge = $('#recipient-age').val();
			this.employeeIdCard = $('#recipient-idCard').val();
			this.employeeAddress = $('#recipient-address').val();
			this.employeePhoneNumber = $('#recipient-phoneNumber').val();
			this.createdBy = $('#username').text();
			this.createdDate = getDateTime(createdDate);
			this.lastModifiedDate = getDateTime(createdDate);
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
					if ($('#findInput').val() === '') {
						ajaxRequest($('.currentPage').text() - 1);
					} else {
						ajaxRequestCondition($('.currentPage').text() - 1, $('#findSelect').val(), $('#findInput').val());
					}
				}
			},
			error: function () {
				alert('添加失败，请检查后重试。');
			}
		})
	})

	// 删除
	$('tbody').on('click', 'tr td .deleteEmployee', function (params) {
		$('.deleteEmployee').blur();
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
						if ($('#findInput').val() === '') {
							ajaxRequest($('.currentPage').text() - 1);
						} else {
							ajaxRequestCondition($('.currentPage').text() - 1, $('#findSelect').val(), $('#findInput').val());
						}
					}
				},
				error: function () {
					$('tbody tr').remove();
					$('tfoot').remove();
					$('tbody').html('<tr><td colspan="12">删除失败，请检查后重试。</td></tr>');
				}
			})
		}
	})

	// 更改
	$('tbody').on('click', 'tr td .updateEmployee', function (params) {

		$('.updateEmployee').blur();
		// 获取当前 count
		const count = params.currentTarget.parentNode.parentNode.firstElementChild.textContent;
		if (regExp(
			$('#trId' + count + ' td:eq(1) label input').val(),
			$('#trId' + count + ' td:eq(4) label input').val(),
			$('#trId' + count + ' td:eq(5) label input').val(),
			$('#trId' + count + ' td:eq(6) label input').val()
		) === false) {return false;}

		if (confirm('确定更改?')) {
			let lastModifiedDate = new Date();
			lastModifiedDate = getDateTime(lastModifiedDate);

			function Employee() {
				this.employeeId = params.currentTarget.parentNode.nextElementSibling.firstElementChild.id;
				this.employeeName = $('#trId' + count + ' td:eq(1) label input').val();
				this.employeeSex = $('#trId' + count + ' td:eq(2) label select').val();
				this.employeeAge = $('#trId' + count + ' td:eq(3) label select').val();
				this.employeeIdCard = $('#trId' + count + ' td:eq(4) label input').val();
				this.employeeAddress = $('#trId' + count + ' td:eq(5) label input').val();
				this.employeePhoneNumber = $('#trId' + count + ' td:eq(6) label input').val();
				this.createdBy = $('#trId' + count + ' td:eq(7)').text();
				this.createdDate = params.currentTarget.parentNode.previousElementSibling.previousElementSibling.textContent;
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
						$('#trId' + count + ' td:eq(9)').html(lastModifiedDate);
					}
				},
				error: function () {
					$('tbody tr').remove();
					$('tfoot').remove();
					$('tbody').html('<tr><td colspan="12">更改失败，请检查后重试。</td></tr>');
				}
			})
		}

	})

	// 查找员工
	// 尝试过把下面两个方法写进一个方法里，但不理想
	// 点击事件
	$('#findButton').on('click', function () {
		if ($('#findInput').val() !== '') {
			$('#findButton').blur();
			findEmployee();
		}
	})
	// 输入完搜索内容后的回车事件
	$('#findInput').on('keydown', function (e) {
		if ($('#findInput').val() !== '') {
			const eCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
			if (eCode === 13) {
				$('#findInput').blur();
				findEmployee();
			}
		}
	})

	// 根据条件查找员工
	function findEmployee() {

		$.ajax({
			type: 'POST',
			// 下面这行非常重要，没有会报错
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
			contentType: 'application/json',
			url: '/employee/findEmployee',
			data: JSON.stringify(new Employee($('#findSelect').val(), $('#findInput').val())),
			// 因为开启了 csrf，所以增加请求头
			// 终于成功了！！！
			headers: {
				// 不区分大小写
				'X-CSRF-Token': getToken()
			},
			success: function (data, success, state) {
				if (state.status === 200 && state.readyState === 4) {
					if (/<option value="男">/.test(data)) {
						// 截取之后填充进页面
						const array1 = data.split('<tbody>');
						const array2 = array1[1].split('</tbody>');
						const array3 = array2[1].split('<div class="modal-footer no-margin-top">');
						const array4 = array3[1].split('</div>');
						const ul = array4[0].replace(/\?/, '/findEmployee?');
						// 截取字符串之后不需要将其转换为 HTML
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
				$('tbody tr').remove();
				$('tfoot').remove();
				$('tbody').html('<tr><td colspan="12">查找失败，请检查后重试。</td></tr>');
			}
		})

	}

	// 首页
	$('tfoot').on('click', 'tr td div ul li #frontPage', function () {
		if ($('#findInput').val() === '') {
			ajaxRequest(0);
		} else {
			ajaxRequestCondition(0, $('#findSelect').val(), $('#findInput').val());
		}
	})
	// 上一页
	$('tfoot').on('click', 'tr td div ul li #previousPageable', function (params) {
		if ($('#findInput').val() === '') {
			ajaxRequest(params.currentTarget.className);
		} else {
			ajaxRequestCondition(params.currentTarget.className, $('#findSelect').val(), $('#findInput').val());
		}
	})
	// 中间页
	$('tfoot').on('click', 'tr td div ul li .midPages', function (params) {
		if ($('#findInput').val() === '') {
			ajaxRequest(params.currentTarget.textContent - 1);
		} else {
			ajaxRequestCondition(params.currentTarget.textContent - 1, $('#findSelect').val(), $('#findInput').val());
		}
	})
	// 下一页
	$('tfoot').on('click', 'tr td div ul li #nextPageable', function (params) {
		if ($('#findInput').val() === '') {
			ajaxRequest(params.currentTarget.className);
		} else {
			ajaxRequestCondition(params.currentTarget.className, $('#findSelect').val(), $('#findInput').val());
		}
	})
	// 尾页
	$('tfoot').on('click', 'tr td div ul li #lastPage', function (params) {
		if ($('#findInput').val() === '') {
			ajaxRequest(params.currentTarget.className);
		} else {
			ajaxRequestCondition(params.currentTarget.className, $('#findSelect').val(), $('#findInput').val());
		}
	})

	// ajax - 无查询条件
	function ajaxRequest(pageNum) {
		if (pageNum > -1) {
			$.ajax({
				type: 'POST',
				url: '/employee?pageNum=' + pageNum,
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						// 如果当前页还有数据
						if (/<option value="男">/.test(data)) {
							updatePage(data);
						} else {
							if (pageNum > 0) {
								$.ajax({
									type: 'POST',
									url: '/employee?pageNum=' + (pageNum - 1),
									// 因为开启了 csrf，所以增加请求头
									headers: {
										'X-CSRF-Token': getToken()
									},
									success: function (data, success, state) {
										if (state.status === 200 && state.readyState === 4) {
											updatePage(data);
										}
									}
								})
							} else {
								$('tbody tr').remove();
								$('tfoot').remove();
								$('tbody').html('<tr><td colspan="12">无匹配结果</td></tr>');
							}
						}
					}
				}
				,
				error: function () {
					$('tbody tr').remove();
					$('tfoot').remove();
					$('tbody').html('<tr><td colspan="12">页面加载失败，请检查后重试。</td></tr>');
				}
			})
		}
	}

	// ajax - 有查询条件
	function ajaxRequestCondition(pageNum, attribute, value) {
		if (pageNum > -1) {
			$.ajax({
				type: 'POST',
				// 下面这行非常重要，没有会报错
				// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
				// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
				contentType: 'application/json',
				url: '/employee/findEmployee?pageNum=' + pageNum,
				data: JSON.stringify(new Employee(attribute, value)),
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						if (/<option value="男">/.test(data)) {
							updatePage(data);
						} else {
							if (pageNum > 0) {
								$.ajax({
									type: 'POST',
									// 下面这行非常重要，没有会报错
									// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
									// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
									contentType: 'application/json',
									url: '/employee/findEmployee?pageNum=' + (pageNum - 1),
									data: JSON.stringify(new Employee(attribute, value)),
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
										$('tbody tr').remove();
										$('tfoot').remove();
										$('tbody').html('<tr><td colspan="12">页面加载失败，请检查后重试。</td></tr>');
									}
								})
							} else {
								$('tbody tr').remove();
								$('tfoot').remove();
								$('tbody').html('<tr><td colspan="12">无匹配结果</td></tr>');
							}
						}
					}
				},
				error: function () {
					$('tbody tr').remove();
					$('tfoot').remove();
					$('tbody').html('<tr><td colspan="12">页面加载失败，请检查后重试。</td></tr>');
				}
			})
		}
	}
}