'use strict';

$(function () {

	const thead = $('thead');
	const tbody = $('tbody');
	const tfoot = $('tfoot');
	const findInput = $('#findInput');
	const modalBody = $('.modal-body');

	// Get token
	function getToken() {
		/*console.log(Symbol());
		console.log(Symbol.prototype);
		console.log(Symbol.prototype.Symbol(Symbol.toStringTag));
		console.log(Symbol.prototype.constructor);
		console.log(Symbol.constructor);
		console.log(Symbol.prototype._csrf);
		console.log(Symbol.prototype.viewport);
		console.log(Symbol.prototype.author);
		console.log(Symbol.prototype.password);
		console.log(Symbol.prototype.username);
		// console.log(Symbol.prototype.description);
		console.log(Symbol.prototype.url);
		console.log(Symbol.prototype.headers);
		console.log(Symbol.prototype.createdBy);
		console.log(Symbol.prototype.type);
		console.log(Symbol.prototype.employeeId);
		console.log(Symbol.prototype.data);
		// console.log(Symbol.prototype.onload());
		console.log(Symbol.prototype.employeeId);
		console.log(Document.viewport);
		console.log(Document.author);
		console.log(Document.username);
		console.log(Document.password);
		console.log(Document.prototype);
		console.log(Document.description);
		console.log(Document.password);
		console.log(Symbol.prototype.isPrototypeOf(Object));
		console.log(Symbol.length);
		console.log(Symbol.name);*/
		return $("input:hidden[name='_csrf']").val();
	}

	// 根据传参判断对象属性
	function Employee() {
		// MySQL 查询默认忽略大小写，所以 findInput.val() 不用转大写
		this[$('#findSelect').val()] = findInput.val();
	}

	// 根据所提供身份证的前 17 位算出最后一位
	function getLastIdCardNumber(id17) {
		const weight = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
		const validate = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
		let sum = 0;
		for (let i = 0; i < id17.length; i++) {
			sum += id17[i] * weight[i];
		}
		return validate[sum % 11];
	}

	// 更新页面数据
	function updatePage(data) {
		if (/updateEmployee/.test(data)) {
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
			const idCard = employeeIdCard.split('');
			idCard.pop();
			if (employeeIdCard.toUpperCase().substring(17) !== getLastIdCardNumber(idCard)) {
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

	// 若有则获取上一次的排序规则
	function getDirectionAndProperty() {
		let direction = 'ASC';
		let property = 'createdDate';
		// 只选取支持排序功能的 th
		for (let i = 1; i < 10; i++) {
			if (/(ASC)|(DESC)/.test(thead.find('tr th:eq(' + i + ')').val())) {
				direction = thead.find('tr th:eq(' + i + ')').val();
				if (i === 1) {
					property = 'employeeName';
					break;
				} else if (i === 2) {
					property = 'employeeSex';
					break;
				} else if (i === 3) {
					property = 'employeeAge';
					break;
				} else if (i === 4) {
					property = 'employeeIdCard';
					break;
				} else if (i === 5) {
					property = 'employeeAddress';
					break;
				} else if (i === 6) {
					property = 'employeePhoneNumber';
					break;
				} else if (i === 7) {
					property = 'createdBy';
					break;
				} else if (i === 8) {
					break;
				} else {
					property = 'lastModifiedDate';
					break;
				}
			}
		}
		return direction + ', ' + property;
	}

	// 添加、修改员工信息
	$(document).on('click', '#saveEmployee', function () {
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
		const birth = arr[1].substring(6, 14);
		const birthYear = birth.substring(0, 4);
		const birthMonth = birth.substring(4, 6);
		const birthDay = birth.substring(6, 8);
		const createdDate = new Date();
		// 'PRC'(People Republic of China): 不区分大小写. '2-digit': 如果为 10 以下则在前面加 0. 只有 month, day 需为 '2-digit'
		const options = {
			timeZone: 'PRC',
			year: 'numeric', month: '2-digit', day: '2-digit',
			hour: 'numeric', minute: 'numeric', second: 'numeric'
		};
		// locales: 不区分大小写.
		const now = createdDate.toLocaleString('zh-CN', options);
		// 2012/02/01 01:01:01
		const nowYear = now.substring(0, 4);
		const nowMonth = now.substring(5, 7);
		const nowDay = now.substring(8, 10);

		function Employee() {
			this['employeeName'] = arr[0];
			// 取消人为操作性别和年龄
			// 改为程序根据身份证判断性别和年龄
			this['employeeSex'] = arr[1].substring(16, 17) % 2 === 0 ? '女' : '男';
			this['employeeAge'] = nowDay - birthDay < 0
				? nowMonth - 1 - birthMonth < 0 ? nowYear - 1 - birthYear : nowYear - birthYear
				: nowMonth - birthMonth < 0 ? nowYear - 1 - birthYear : nowYear - birthYear;
			this['employeeIdCard'] = arr[1].toUpperCase();
			this['employeeAddress'] = arr[2];
			this['employeePhoneNumber'] = arr[3];
			this['lastModifiedDate'] = createdDate.toLocaleString('zh-CN', options);
			// 添加员工
			if (modalBody.val() === undefined || modalBody.val() === '' || modalBody.val() === null) {
				this['createdBy'] = $('#username').text();
				this['createdDate'] = createdDate.toLocaleString('zh-CN', options);
			} else {
				// 修改员工
				this['employeeId'] = tbody.find('tr:eq(' + modalBody.val() + ') th').attr('id');
				this['createdBy'] = tbody.find('tr:eq(' + modalBody.val() + ') td:eq(6)').text();
				this['createdDate'] = tbody.find('tr:eq(' + modalBody.val() + ') td:eq(7)').text();
				modalBody.val('');
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
					saveOrDelete();
				}
			},
			error: function () {
				new bootstrap.Toast($('#liveToast')).show();
			}
		})
	})

	// 删除
	// 在这里, $(document) 不可简写
	$(document).on('click', '.deleteEmployee', function () {
		this.blur();
		const employeeId = this.parentElement.parentElement.firstElementChild.id;
		if (employeeId === undefined || employeeId === '' || employeeId === null) {
			$('.toast-body').text('没有获取到员工 ID，请检查后重试。');
			new bootstrap.Toast($('#liveToast')).show();
			return false;
		}
		if (confirm('确定删除?')) {
			$.ajax({
				type: 'POST',
				// 最前面必须要加斜杠/
				url: '/employee/deleteEmployee?employeeId=' + employeeId,
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						saveOrDelete();
					}
				},
				error: function () {
					$('.toast-body').text('删除失败，请检查后重试。');
					new bootstrap.Toast($('#liveToast')).show();
				}
			})
		}
	})

	// 添加、修改或者删除操作之后的查询是一致的
	function saveOrDelete() {
		const strings = getDirectionAndProperty().split(', ');
		if (findInput.val() === '') {
			ajaxRequest($('.currentPage').attr('name'), strings[0], strings[1]);
		} else {
			ajaxRequestCondition($('.currentPage').attr('name'), strings[0], strings[1]);
		}
	}

	// 将需要更改的员工信息填充进 modal
	$(document).on('click', '.updateEmployee', function () {
		// 可能是因为唤出过 modal，而无法失焦
		this.blur();
		// 获取当前 tbody tr 下标
		const index = this.parentNode.parentNode.firstElementChild.textContent % 10 - 1;
		modalBody.val(index);
		$('#recipient-name').val(tbody.find('tr:eq(' + index + ') td:eq(0)').text());
		$('#recipient-idCard').val(tbody.find('tr:eq(' + index + ') td:eq(3)').text());
		$('#recipient-address').val(tbody.find('tr:eq(' + index + ') td:eq(4)').text());
		$('#recipient-phoneNumber').val(tbody.find('tr:eq(' + index + ') td:eq(5)').text());
	})
	// 避免点击更改弹出了 modal 更改后却并没有提交使 tbody tr 下标继续留在 .modal-body
	$(document).on('click', '.close-employee-modal', function () {
		modalBody.val('');
	})

	// 查找员工
	// 尝试过把下面两个方法写进一个方法里，但不理想
	// 点击事件
	$(document).on('click', '#findButton', function () {
		if (this.previousElementSibling.value !== '') {
			this.blur();
			findEmployee();
		}
	})
	// 输入完搜索内容后的回车事件
	$(document).on('keydown', '#findInput', function (e) {
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
		// 每次查找之前都重置数据
		employeeName = 0;
		employeeSex = 0;
		employeeAge = 0;
		employeeIdCard = 0;
		employeeAddress = 0;
		employeePhoneNumber = 0;
		createdBy = 0;
		createdDate = 0;
		lastModifiedDate = 0;
		for (let i = 1; i < 10; i++) {
			$('thead tr th:eq(' + i + ')').val('');
		}
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

	// 根据排序条件查询
	// 写在这里方便查看
	let employeeName = 0;
	let employeeSex = 0;
	let employeeAge = 0;
	let employeeIdCard = 0;
	let employeeAddress = 0;
	let employeePhoneNumber = 0;
	let createdBy = 0;
	let createdDate = 0;
	let lastModifiedDate = 0;
	$(document).on('click', 'thead tr th', function () {
		if (this.cellIndex === 1) {
			if (employeeName === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'employeeName');
				} else {
					sortDirectionCondition('ASC', 'employeeName');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('ASC');
				employeeName = 1;
			} else {
				if (findInput.val() === '') {
					sortDirection('DESC', 'employeeName');
				} else {
					sortDirectionCondition('DESC', 'employeeName');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('DESC');
				employeeName = 0;
			}
		} else if (this.cellIndex === 2) {
			if (employeeSex === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'employeeSex');
				} else {
					sortDirectionCondition('ASC', 'employeeSex');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('ASC');
				employeeSex = 1;
			} else {
				if (findInput.val() === '') {
					sortDirection('DESC', 'employeeSex');
				} else {
					sortDirectionCondition('DESC', 'employeeSex');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('DESC');
				employeeSex = 0;
			}
		} else if (this.cellIndex === 3) {
			if (employeeAge === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'employeeAge');
				} else {
					sortDirectionCondition('ASC', 'employeeAge');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('ASC');
				employeeAge = 1;
			} else {
				if (findInput.val() === '') {
					sortDirection('DESC', 'employeeAge');
				} else {
					sortDirectionCondition('DESC', 'employeeAge');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('DESC');
				employeeAge = 0;
			}
		} else if (this.cellIndex === 4) {
			if (employeeIdCard === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'employeeIdCard');
				} else {
					sortDirectionCondition('ASC', 'employeeIdCard');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('ASC');
				employeeIdCard = 1;
			} else {
				if (findInput.val() === '') {
					sortDirection('DESC', 'employeeIdCard');
				} else {
					sortDirectionCondition('DESC', 'employeeIdCard');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('DESC');
				employeeIdCard = 0;
			}
		} else if (this.cellIndex === 5) {
			if (employeeAddress === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'employeeAddress');
				} else {
					sortDirectionCondition('ASC', 'employeeAddress');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('ASC');
				employeeAddress = 1;
			} else {
				if (findInput.val() === '') {
					sortDirection('DESC', 'employeeAddress');
				} else {
					sortDirectionCondition('DESC', 'employeeAddress');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('DESC');
				employeeAddress = 0;
			}
		} else if (this.cellIndex === 6) {
			if (employeePhoneNumber === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'employeePhoneNumber');
				} else {
					sortDirectionCondition('ASC', 'employeePhoneNumber');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('ASC');
				employeePhoneNumber = 1;
			} else {
				if (findInput.val() === '') {
					sortDirection('DESC', 'employeePhoneNumber');
				} else {
					sortDirectionCondition('DESC', 'employeePhoneNumber');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('DESC');
				employeePhoneNumber = 0;
			}
		} else if (this.cellIndex === 7) {
			if (createdBy === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'createdBy');
				} else {
					sortDirectionCondition('ASC', 'createdBy');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('ASC');
				createdBy = 1;
			} else {
				if (findInput.val() === '') {
					sortDirection('DESC', 'createdBy');
				} else {
					sortDirectionCondition('DESC', 'createdBy');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('DESC');
				createdBy = 0;
			}
		} else if (this.cellIndex === 8) {
			if (createdDate === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'createdDate');
				} else {
					sortDirectionCondition('ASC', 'createdDate');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('ASC');
				createdDate = 1;
			} else {
				if (findInput.val() === '') {
					sortDirection('DESC', 'createdDate');
				} else {
					sortDirectionCondition('DESC', 'createdDate');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('DESC');
				createdDate = 0;
			}
		} else if (this.cellIndex === 9) {
			if (lastModifiedDate === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'lastModifiedDate');
				} else {
					sortDirectionCondition('ASC', 'lastModifiedDate');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('ASC');
				lastModifiedDate = 1;
			} else {
				if (findInput.val() === '') {
					sortDirection('DESC', 'lastModifiedDate');
				} else {
					sortDirectionCondition('DESC', 'lastModifiedDate');
				}
				deleteOtherTheadTrThVal(this.cellIndex);
				$(this).val('DESC');
				lastModifiedDate = 0;
			}
		}
	})

	// 执行指定的排序查询————无查询条件
	function sortDirection(direction, property) {
		$.ajax({
			type: 'POST',
			// 一定要记得 `=` 符号!
			url: '/employee?direction=' + direction + '&property=' + property,
			// 因为开启了 csrf，所以增加请求头
			headers: {
				'X-CSRF-Token': getToken()
			},
			success: function (data, success, state) {
				if (state.status === 200 && state.readyState === 4) {
					updatePage(data);
				}
			}
		});
	}

	// 执行指定的排序查询————有查询条件，或者当只有一页数据并添加数据时会通过这里查询
	function sortDirectionCondition(direction, property) {
		$.ajax({
			type: 'POST',
			// 下面这行非常重要，没有会报错
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
			contentType: 'application/json',
			url: '/employee/findEmployee?direction=' + direction + '&property=' + property,
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
		});
	}

	// 删除其他 thead tr th 的 val()
	function deleteOtherTheadTrThVal(cellIndex) {
		let ths = [];
		// 只选取支持排序功能的 th
		for (let i = 1; i < 10; i++) {
			ths.push($('thead tr th:eq(' + i + ')'));
		}
		// 不包括自身 this.cellIndex - 1
		ths = ths.splice(cellIndex - 1, 1);
		// 另外一种写法
		/*ths = $.grep(ths, function (n, i) {
			return i !== cellIndex - 1;
		})*/
		for (let i = 0; i < ths.length; i++) {
			ths[i].val('');
		}
	}

	// 首页、上一页、中间页、下一页、尾页
	$(document).on('click', '.page-link', function () {
		const strings = getDirectionAndProperty().split(', ');
		if (findInput.val() === '') {
			ajaxRequest(this.name, strings[0], strings[1]);
		} else {
			ajaxRequestCondition(this.name, strings[0], strings[1]);
		}
	})

	// ajax - 无查询条件
	function ajaxRequest(pageNum, direction, property) {
		if (pageNum > -1) {
			$.ajax({
				type: 'POST',
				url: '/employee?pageNum=' + pageNum + '&direction=' + direction + '&property=' + property,
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						// 如果当前页还有数据
						if (/updateEmployee/.test(data)) {
							updatePage(data);
						} else {
							if (pageNum > 0) {
								$.ajax({
									type: 'POST',
									url: '/employee?pageNum=' + (pageNum - 1) + '&direction=' + direction + '&property=' + property,
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
				},
				error: function () {
					$('.toast-body').text('页面加载失败，请检查后重试。');
					new bootstrap.Toast($('#liveToast')).show();
				}
			})
		} else {
			sortDirection(direction, property);
		}
	}

	// ajax - 有查询条件
	function ajaxRequestCondition(pageNum, direction, property) {
		if (pageNum > -1) {
			$.ajax({
				type: 'POST',
				// 下面这行非常重要，没有会报错
				// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
				// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
				contentType: 'application/json',
				url: '/employee/findEmployee?pageNum=' + pageNum + '&direction=' + direction + '&property=' + property,
				data: JSON.stringify(new Employee()),
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': getToken()
				},
				success: function (data, success, state) {
					if (state.status === 200 && state.readyState === 4) {
						if (/updateEmployee/.test(data)) {
							updatePage(data);
						} else {
							if (pageNum > 0) {
								$.ajax({
									type: 'POST',
									// 下面这行非常重要，没有会报错
									// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
									// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
									contentType: 'application/json',
									url: '/employee/findEmployee?pageNum=' + (pageNum - 1) + '&direction=' + direction + '&property=' + property,
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
			sortDirectionCondition(direction, property);
		}
	}
});

window.onload = function () {
};