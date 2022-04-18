'use strict';

$(function () {

	const tbody = $($).find('tbody');
	const tfoot = $($).find('tfoot');
	const findInput = $($).find('#findInput');
	const modalBody = $($).find('.modal-body');

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
			tfoot.find('tr td div').children().remove();
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
		if (employeeName === '') {
			$('.toast-body').text('姓名不能为空。');
			new bootstrap.Toast($('#liveToast')).show();
			return false;
		}
		if (!/^[\u4e00-\u9fa5\w\s•]{1,25}$/.test(employeeName)) {
			$('.toast-body').text('姓名只支持由 1 - 25 个汉字、英文、数字、空格和•的组合。');
			new bootstrap.Toast($('#liveToast')).show();
			return false;
		}

		// 验证身份证号码
		if (employeeIdCard === '') {
			$('.toast-body').text('身份证号码不能为空。');
			new bootstrap.Toast($('#liveToast')).show();
			return false;
		}
		if (employeeIdCard.length !== 15 && employeeIdCard.length !== 18) {
			$('.toast-body').text('请填写 15 或者 18 位身份证号码。');
			new bootstrap.Toast($('#liveToast')).show();
			return false;
		}
		// 15、18 位身份证号码第一重验证
		if (!/^\d{15}|\d{18}|(\d{17}X|x)$/.test(employeeIdCard)) {
			$('.toast-body').text('身份证号码有误，请检查后重试。');
			new bootstrap.Toast($('#liveToast')).show();
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
				$('.toast-body').text('身份证号码有误，请检查后重试。');
				new bootstrap.Toast($('#liveToast')).show();
				return false;
			}
		}

		// 验证住址
		if (employeeAddress === '') {
			$('.toast-body').text('住址不能为空。');
			new bootstrap.Toast($('#liveToast')).show();
			return false;
		}
		if (!/^[\u4e00-\u9fa5\w\s•,]{2,45}$/.test(employeeAddress)) {
			$('.toast-body').text('住址只支持由最多 45 个汉字、英文、空格、英文逗号和•的组合。');
			new bootstrap.Toast($('#liveToast')).show();
			return false;
		}

		// 验证电话号码（暂时只验证在一般情况下的中国大陆移动手机号码）
		if (employeePhoneNumber === '') {
			$('.toast-body').text('电话号码不能为空。');
			new bootstrap.Toast($('#liveToast')).show();
			return false;
		}
		if (!/^1[3-9]\d{9}$/.test(employeePhoneNumber)) {
			$('.toast-body').text('电话号码格式有误，请检查后重试。');
			new bootstrap.Toast($('#liveToast')).show();
			return false;
		}
	}

	// 添加、修改员工信息
	$('#saveEmployee').on('click', function () {
		// 可能是因为唤出过 modal，而无法失焦
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
		if (regExp(arr[0], arr[1], arr[2], arr[3]) === false) {
			return false;
		}
		const createdDate = new Date();
		const birth = arr[1].substring(6, 14);
		const birthYear = birth.substring(0, 4);
		const birthMonth = birth.substring(4, 6);
		const birthDay = birth.substring(6, 8);
		const now = getDateTime(createdDate).substring(0, 10).replace(/-/g, '');
		const nowYear = now.substring(0, 4);
		const nowMonth = now.substring(4, 6);
		const nowDay = now.substring(6, 8);

		function Employee() {
			this['employeeName'] = arr[0];
			// 取消人为操作性别和年龄
			// 改为根据身份证判断性别和年龄
			this['employeeSex'] = arr[1].substring(16, 17) % 2 === 0 ? '女' : '男';
			this['employeeAge'] = nowDay - birthDay < 0 ? nowMonth - 1 - birthMonth < 0 ? nowYear - 1 - birthYear : nowYear - birthYear : nowMonth - birthMonth < 0 ? nowYear - 1 - birthYear : nowYear - birthYear;
			this['employeeIdCard'] = arr[1].toUpperCase();
			this['employeeAddress'] = arr[2];
			this['employeePhoneNumber'] = arr[3];
			this['lastModifiedDate'] = getDateTime(createdDate);
			// 添加员工
			if (modalBody.attr('id') === undefined || modalBody.attr('id') === '' || modalBody.attr('id') === null) {
				this['createdBy'] = $('#username').text();
				this['createdDate'] = getDateTime(createdDate);
			} else {
				// 修改员工
				this['employeeId'] = tbody.find('tr:eq(' + modalBody.attr('id') + ') th').attr('id');
				this['createdBy'] = tbody.find('tr:eq(' + modalBody.attr('id') + ') td:eq(6)').text();
				this['createdDate'] = tbody.find('tr:eq(' + modalBody.attr('id') + ') td:eq(7)').text();
				modalBody.removeAttr('id');
			}
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
				new bootstrap.Toast($('#liveToast')).show();
			}
		})
	})

	// 删除
	tbody.on('click', '.deleteEmployee', function () {
		this.blur();
		const employeeId = this.parentElement.parentElement.firstElementChild.id;
		if (employeeId === '' || employeeId === null) {
			$('.toast-body').text('系统出现故障，请检查后重试。');
			new bootstrap.Toast($('#liveToast')).show();
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
					$('.toast-body').text('删除失败，请检查后重试。');
					new bootstrap.Toast($('#liveToast')).show();
				}
			})
		}
	})

	// 将需要更改的员工信息填充进 modal
	tbody.on('click', '.updateEmployee', function () {
		// 可能是因为唤出过 modal，而无法失焦
		this.blur();
		// 获取当前 tbody tr 下标
		const index = this.parentNode.parentNode.firstElementChild.textContent % 10 - 1;
		modalBody.attr('id', index);
		$('#recipient-name').val(tbody.find('tr:eq(' + index + ') td:eq(0)').text());
		$('#recipient-idCard').val(tbody.find('tr:eq(' + index + ') td:eq(3)').text());
		$('#recipient-address').val(tbody.find('tr:eq(' + index + ') td:eq(4)').text());
		$('#recipient-phoneNumber').val(tbody.find('tr:eq(' + index + ') td:eq(5)').text());
	})

	$('.close-employee-modal').on('click', function () {
		modalBody.removeAttr('id');
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
				$('.toast-body').text('查找失败，请检查后重试。');
				new bootstrap.Toast($('#liveToast')).show();
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
								tfoot.find('tr td div').children().remove();
							}
						}
					}
				}
				,
				error: function () {
					$('.toast-body').text('页面加载失败，请检查后重试。');
					new bootstrap.Toast($('#liveToast')).show();
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
										$('.toast-body').text('页面加载失败，请检查后重试。');
										new bootstrap.Toast($('#liveToast')).show();
									}
								})
							} else {
								tbody.children().remove();
								tfoot.find('tr td div').children().remove();
							}
						}
					}
				},
				error: function () {
					$('.toast-body').text('页面加载失败，请检查后重试。');
					new bootstrap.Toast($('#liveToast')).show();
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