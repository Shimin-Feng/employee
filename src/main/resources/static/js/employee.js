'use strict';

$(function () {

	const tbody = $($).find('tbody');
	const tfoot = $($).find('tfoot');
	const findInput = $($).find('#findInput');

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
		if (/deleteEmployee/.test(data)) {
			// 截取之后填充进页面
			const trs1 = data.split('<tbody class="table-secondary">');
			const trs2 = trs1[1].split('</tbody>');
			const ul1 = trs2[1].split('<div class="modal-footer no-margin-top">');
			const ul2 = ul1[1].split('</div>');
			tbody.html(trs2[0]);
			$('.no-margin-top').html(ul2[0]);
		} else {
			tbody.children().remove();
			tfoot.remove();
			tbody.html('<tr><td colspan="12">无匹配结果</td></tr>');
		}
	}

	// 根据传参判断对象属性
	function Employee() {
		this[$('#findSelect').val()] = findInput.val();
	}

	// Get token
	function getToken() {
		return $("input:hidden[name='_csrf']").val();
	}

	// 验证信息
	function regExp(employeeName, employeeIdCard, employeeAddress, employeePhoneNumber) {
		// 验证姓名
		if (!/^[\u4e00-\u9fa5\w\s•]{1,25}$/.test(employeeName)) {
			alert('姓名只支持由 1 - 25 个汉字、英文、数字、空格和•的组合。');
			return false;
		}

		// 验证身份证
		if (employeeIdCard.length !== 15 && employeeIdCard.length !== 18) {
			alert('请填写 15 或者 18 位身份证号码。');
			return false;
		}
		// 15、18 位身份证号码第一重验证
		if (!/^\d{15}|\d{18}|(\d{17}X|x)$/.test(employeeIdCard)) {
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
		if (!/^[\u4e00-\u9fa5\w\s•,]{2,45}$/.test(employeeAddress)) {
			alert('住址只支持由最多 45 个汉字、英文、空格、英文逗号和•的组合。');
			return false;
		}

		// 验证电话号码（暂时只验证在一般情况下的中国大陆移动手机号码）
		if (!/^1[3-9]\d{9}$/.test(employeePhoneNumber)) {
			alert('电话号码格式有误，请检查后重试。');
			return false;
		}
	}

	// 添加员工
	$('#saveEmployee').on('click', function () {
		/*console.log($);
		console.log($());
		console.log($($));
		console.log($(document));
		$(document).blur();*/
		$('#save').blur();
		// 获取表单数据
		let arr = [];
		$('.form-control').each(function () {
			arr.push($(this).val());
		})
		// 删除第一个不需要的值
		arr.shift();
		// 验证值
		if (regExp(arr[0], arr[3], arr[4], arr[5]) === false) {
			return false;
		}
		const createdDate = new Date();

		function Employee() {
			this['employeeName'] = arr[0];
			this['employeeSex'] = arr[1];
			this['employeeAge'] = arr[2];
			this['employeeIdCard'] = arr[3].toUpperCase();
			this['employeeAddress'] = arr[4];
			this['employeePhoneNumber'] = arr[5];
			this['createdBy'] = $('#username').text();
			this['lastModifiedDate'] = this['createdDate'] = getDateTime(createdDate);
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
					if (findInput.val() === '') {
						ajaxRequest($('.currentPage').attr('name'));
					} else {
						ajaxRequestCondition($('.currentPage').attr('name'));
					}
				}
			},
			error: function () {
				alert('添加失败，请检查后重试。');
			}
		})
	})

	// 删除
	tbody.on('click', '.deleteEmployee', function () {
		this.blur();
		const employeeId = this.parentElement.parentElement.firstElementChild.id;
		if (employeeId === '' || employeeId === null) {
			alert('系统出现故障，请检查后重试。');
			return false;
		}
		if (confirm('确定删除?')) {
			$.ajax({
				type: 'POST',
				// 最前面必须要加斜杠/ '/employee/deleteEmployee/'
				url: '/employee/deleteEmployee',
				data: {'employeeId': employeeId},
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						if (findInput.val() === '') {
							ajaxRequest($('.currentPage').attr('name'));
						} else {
							ajaxRequestCondition($('.currentPage').attr('name'));
						}
					}
				},
				error: function () {
					tbody.children().remove();
					tfoot.remove();
					tbody.html('<tr><td colspan="12">删除失败，请检查后重试。</td></tr>');
				}
			})
		}
	})

	// 更改
	tbody.on('click', '.updateEmployee', function () {
		this.blur();
		// 获取当前 tbody tr 下标
		const index = this.parentNode.parentNode.firstElementChild.textContent % 10 - 1;
		if (regExp(
			tbody.find('tr:eq(' + index + ') td:eq(0) label input').val(),
			tbody.find('tr:eq(' + index + ') td:eq(3) label input').val(),
			tbody.find('tr:eq(' + index + ') td:eq(4) label input').val(),
			tbody.find('tr:eq(' + index + ') td:eq(5) label input').val()
		) === false) {
			return false;
		}

		if (confirm('确定更改?')) {
			let lastModifiedDate = new Date();
			lastModifiedDate = getDateTime(lastModifiedDate);

			function Employee() {
				this['employeeId'] = tbody.find('tr:eq(' + index + ') th').attr('id');
				this['employeeName'] = tbody.find('tr:eq(' + index + ') td:eq(0) label input').val();
				this['employeeSex'] = tbody.find('tr:eq(' + index + ') td:eq(1) label select').val();
				this['employeeAge'] = tbody.find('tr:eq(' + index + ') td:eq(2) label select').val();
				this['employeeIdCard'] = tbody.find('tr:eq(' + index + ') td:eq(3) label input').val().toUpperCase();
				this['employeeAddress'] = tbody.find('tr:eq(' + index + ') td:eq(4) label input').val();
				this['employeePhoneNumber'] = tbody.find('tr:eq(' + index + ') td:eq(5) label input').val();
				this['createdBy'] = tbody.find('tr:eq(' + index + ') td:eq(6)').text();
				this['createdDate'] = tbody.find('tr:eq(' + index + ') td:eq(7)').text();
				this['lastModifiedDate'] = lastModifiedDate;
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
						$('tbody tr:eq(' + index + ') td:eq(8)').html(lastModifiedDate);
					}
				},
				error: function () {
					tbody.children().remove();
					tfoot.remove();
					tbody.html('<tr><td colspan="12">更改失败，请检查后重试。</td></tr>');
				}
			})
		}

	})

	// 查找员工
	// 尝试过把下面两个方法写进一个方法里，但不理想
	// 点击事件
	$('#findButton').on('click', function () {
		if (this.previousElementSibling.value !== '') {
			this.blur();
			findEmployee();
		}
	})
	// 输入完搜索内容后的回车事件
	findInput.on('keydown', function (e) {
		if (this.value !== '') {
			const keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
			if (keyCode === 13) {
				this.blur();
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
			data: JSON.stringify(new Employee()),
			// 因为开启了 csrf，所以增加请求头
			// 终于成功了！！！
			headers: {
				// 不区分大小写
				'X-CSRF-Token': getToken()
			},
			success: function (data, success, state) {
				if (state.status === 200 && state.readyState === 4) {
					updatePage(data);
				}
			},
			error: function () {
				tbody.children().remove();
				tfoot.remove();
				tbody.html('<tr><td colspan="12">查找失败，请检查后重试。</td></tr>');
			}
		})

	}

	// 首页、上一页、中间页、下一页、尾页
	tfoot.on('click', '.page-link', function () {
		if (findInput.val() === '') {
			ajaxRequest(this.name);
		} else {
			ajaxRequestCondition(this.name);
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
						if (/deleteEmployee/.test(data)) {
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
								tbody.children().remove();
								tfoot.remove();
								tbody.html('<tr><td colspan="12">无匹配结果</td></tr>');
							}
						}
					}
				}
				,
				error: function () {
					tbody.children().remove();
					tfoot.remove();
					tbody.html('<tr><td colspan="12">页面加载失败，请检查后重试。</td></tr>');
				}
			})
		} else {
			$.ajax({
				type: 'POST',
				url: '/employee',
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
		}
	}

	// ajax - 有查询条件
	function ajaxRequestCondition(pageNum) {
		if (pageNum > -1) {
			$.ajax({
				type: 'POST',
				// 下面这行非常重要，没有会报错
				// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
				// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
				contentType: 'application/json',
				url: '/employee/findEmployee?pageNum=' + pageNum,
				data: JSON.stringify(new Employee()),
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						if (/deleteEmployee/.test(data)) {
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
									data: JSON.stringify(new Employee()),
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
										tbody.children().remove();
										tfoot.remove();
										tbody.html('<tr><td colspan="12">页面加载失败，请检查后重试。</td></tr>');
									}
								})
							} else {
								tbody.children().remove();
								tfoot.remove();
								tbody.html('<tr><td colspan="12">无匹配结果</td></tr>');
							}
						}
					}
				},
				error: function () {
					tbody.children().remove();
					tfoot.remove();
					tbody.html('<tr><td colspan="12">页面加载失败，请检查后重试。</td></tr>');
				}
			})
		} else {
			$.ajax({
				type: 'POST',
				// 下面这行非常重要，没有会报错
				// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
				// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
				contentType: 'application/json',
				url: '/employee/findEmployee',
				data: JSON.stringify(new Employee()),
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
		}
	}
});

window.onload = function () {
}