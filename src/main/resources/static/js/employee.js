!function () {

	'use strict'

	const thead = $('thead'),
		tbody = $('tbody'),
		tfoot = $('tfoot'),
		modalBody = $('.modal-body'),
		findSelect = $('#findSelect'),
		findInput = $('#findInput'),
		toastBody = $('.toast-body'),
		liveToast = $('#liveToast'),

		// Get token
		token = $('input:hidden[name="_csrf"]').val()

	// 根据传参判断对象属性
	function Employee() {
		// MySQL 查询默认忽略大小写，所以 findInput.val() 不用转大写
		this[findSelect.val()] = findInput.val()
	}

	// 根据所提供身份证的前 17 位算出最后一位
	function getLastIdCardNumber(id17) {
		const weight = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2],
			validate = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2']
		let sum = 0
		for (let i = 0; i < id17.length; i++) {
			sum += id17[i] * weight[i]
		}
		return validate[sum % 11]
	}

	// 更新页面数据
	function updatePage(data) {
		if (/updateEmployee/.test(data)) {
			// 截取之后填充进页面
			const trs = data.split('<tbody class="table-secondary">')[1].split('</tbody>'),
				ul = trs[1].split('<div class="modal-footer no-margin-top">')[1].split('</div>')
			tbody.html(trs[0])
			$('.no-margin-top').html(ul[0])
		} else {
			tbody.children().remove()
			tfoot.find('tr td div').children().remove()
		}
	}

	// 验证信息
	function regExp(employeeName, employeeIdCard, employeeAddress, employeePhoneNumber) {
		// 首先验证是否存在空值
		// 获取表单数据
		const arr = []
		$('.form-control').each(function () {
			arr.push($(this).val())
		})
		// 删除第一个不需要的值
		arr.shift()

		let msg = ''
		for (let i in arr) {
			if ('' === arr[i]) {
				switch (i) {
					case '0':
						msg = '姓名不能为空。'
						break
					case '1':
						msg = '身份证号码不能为空。'
						break
					case '2':
						msg = '住址不能为空。'
						break
					case '3':
						msg = '电话号码不能为空。'
				}
				break
			}
		}
		if ('' === msg) {
			// 验证姓名
			if (!/^[\u4e00-\u9fa5\w\s•]{1,25}$/.test(employeeName)) {
				msg = '姓名只支持由 1 - 25 个汉字、英文、数字、空格和•的组合。'
			}

			// 验证身份证号码，15、18 位身份证号码第一重验证
			else if (employeeIdCard.length !== 15 && employeeIdCard.length !== 18) {
				msg = '请填写 15 或者 18 位身份证号码。'
			}
			// 15、18 位身份证号码第二重验证
			else if (!/^\d{15}|\d{18}|(\d{17}X|x)$/.test(employeeIdCard)) {
				msg = '身份证号码有误，请检查后重试。'
			}
			// 18 位身份证号码第三重验证
			else if (employeeIdCard.length === 18 && employeeIdCard.charAt(17).toUpperCase() !== getLastIdCardNumber(employeeIdCard.substring(0, 17).split(''))) {
				msg = '身份证号码有误，请检查后重试。'
			}

			// 验证住址
			else if (!/^[\u4e00-\u9fa5\w\s•,]{2,45}$/.test(employeeAddress)) {
				msg = '住址只支持由最多 45 个汉字、英文、空格、英文逗号和•的组合。'
			}

			// 验证电话号码（暂时只验证在一般情况下的中国大陆移动手机号码）
			else if (!/^1[3-9]\d{9}$/.test(employeePhoneNumber)) {
				msg = '电话号码格式有误，请检查后重试。'
			}
		}
		if ('' !== msg) {
			toastBody.text(msg);
			new bootstrap.Toast(liveToast).show();
			return false;
		}
	}

	// 若有则获取上一次的排序规则
	function getDirectionAndProperty() {
		let direction = 'ASC',
			property = 'createdDate'
		// 只选取支持排序功能的 th
		for (let i = 1; i < 10; i++) {
			if (/^(ASC|DESC)$/.test(thead.find('tr th:eq(' + i + ')').val())) {
				direction = thead.find('tr th:eq(' + i + ')').val()
				switch (i) {
					case 1:
						property = 'employeeName'
						break
					case 2:
						property = 'employeeSex'
						break
					case 3:
						property = 'employeeAge'
						break
					case 4:
						property = 'employeeIdCard'
						break
					case 5:
						property = 'employeeAddress'
						break
					case 6:
						property = 'employeePhoneNumber'
						break
					case 7:
						property = 'createdBy'
						break
					case 9:
						property = 'lastModifiedDate'
				}
				break
			}
		}
		return direction + ', ' + property
	}

	// 添加、修改员工信息
	$(document).on('click', '#saveOrUpdateEmployee', function () {
		// 获取表单数据
		const arr = []
		$('.form-control').each(function () {
			arr.push($(this).val())
		})
		// 删除第一个不需要的值
		arr.shift()
		// 验证值
		if (regExp(arr[0], arr[1], arr[2], arr[3]) === false) {
			return
		}

		// 部分操作转到后台
		function Employee() {
			this.employeeName = arr[0]
			this.employeeIdCard = arr[1]
			this.employeeAddress = arr[2]
			this.employeePhoneNumber = arr[3]
			if (/\d/.test(modalBody.val())) {
				// 修改员工
				this['employeeId'] = tbody.find('tr:eq(' + modalBody.val() + ') th').attr('id')
				modalBody.val('')
			}
		}

		$.ajax({
			type: 'POST',
			// 下面这行非常重要，没有会报错
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
			contentType: 'application/json',
			url: '/employee/saveOrUpdateEmployee',
			data: JSON.stringify(new Employee()),
			// 因为开启了 csrf，所以增加请求头
			headers: {
				'X-CSRF-Token': token
			},
			success: function (data, success, state) {
				console.log(data)
				if (state.readyState === 4 && state.status === 200) {
					saveOrDelete()
				}
			},
			error: function (state) {
				console.log(state)
				console.log(state.status)
				console.log(state.statusText)
				console.log(state.responseText)
				toastBody.text(state.responseText)
				new bootstrap.Toast(liveToast).show()
			}
		})
	})

	// 删除
	// 在这里, $(document) 不可简写
	$(document).on('click', '.deleteEmployeeById', function () {
		const employeeId = this.parentElement.parentElement.firstElementChild.id
		if (!/^\w{8}-\w{4}-\w{4}-\w{4}-\w{12}$/.test(employeeId)) {
			toastBody.text('没有获取到员工 ID，请检查后重试。')
			new bootstrap.Toast(liveToast).show()
			return
		}
		if (confirm('确定删除?')) {
			$.ajax({
				type: 'POST',
				// 最前面必须要加斜杠/
				url: '/employee/deleteEmployeeById?employeeId=' + employeeId,
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': token
				},
				success: function (data, success, state) {
					console.log(data)
					if (state.readyState === 4 && state.status === 200) {
						saveOrDelete()
					}
				},
				error: function (state) {
					console.log(state)
					console.log(state.status)
					console.log(state.statusText)
					console.log(state.responseText)
					toastBody.text(state.responseText)
					new bootstrap.Toast(liveToast).show()
				}
			})
		}
	})

	// 添加、修改或者删除操作之后的查询是一致的
	function saveOrDelete() {
		const strings = getDirectionAndProperty().split(', ')
		ajaxRequestCondition($('.currentPage').attr('name'), strings[0], strings[1])
	}

	// 将需要更改的员工信息填充进 modal
	$(document).on('click', '.updateEmployee', function () {
		// 获取当前 tbody tr 下标
		const index = this.parentNode.parentNode.firstElementChild.textContent % 10 - 1
		modalBody.val(index)
		$('#recipient-name').val(tbody.find('tr:eq(' + index + ') td:eq(0)').text())
		$('#recipient-idCard').val(tbody.find('tr:eq(' + index + ') td:eq(3)').text())
		$('#recipient-address').val(tbody.find('tr:eq(' + index + ') td:eq(4)').text())
		$('#recipient-phoneNumber').val(tbody.find('tr:eq(' + index + ') td:eq(5)').text())
	})
	// 避免点击更改弹出了 modal 更改后却并没有提交使 tbody tr 下标继续留在 .modal-body
	$(document).on('click', '.close-employee-modal', function () {
		modalBody.val('')
	})

	// jQuery UI autocomplete 动态提示搜索建议
	findInput.autocomplete({
		// 触发该事件的最短字符数
		minLength: 0,
		'source': function (request, response) {
			$.ajax({
				type: 'POST',
				url: '/findRecordNamesBy',
				data: {
					'searchGroupBy': findSelect.val(),
					'recordName': request.term
				},
				dataType: 'json',
				headers: {
					// 不区分大小写
					'X-CSRF-Token': token
				},
				success: function (data, success, state) {
					if (state.readyState === 4 && state.status === 200) {
						response(data)
						/*$('#ui-id-2').prepend('<div class="delete-record-nane" style="background-color: #ff3300">test-before</div>')
							.append('<div class="delete-record-nane" style="background-color: #ff3300">test-after</div>')*/
					}
				},
				error: function (state, error, data) {
					console.log(state)
					console.log(state.readyState)
					console.log(state.status)
					console.log(error)
					console.log(data)
					findInput.autocomplete('close')
				}
			})
		},
		'select': function (event, ui) {
			// 以下两个属性都可以正确获取值
			// ui.item.label
			// ui.item.value
			$(this).val(ui.item.value)
			this.blur()
			findEmployeesBy()
			// 如果不 return，jQuery UI 会继续执行自己的 select 方法
			return false
		}
	}).focus(function () {
		// 该函数作用：第一次点击输入框就触发该事件
		$(this).autocomplete('search', $(this).val())
	})

	// 查找员工
	// 尝试过把下面两个方法写进一个方法里，但不理想
	// 键盘回车事件
	$(document).on('keypress', '#findInput', function (e) {
		// keypress 相对于 keydown 与 keyup，只有按下 Enter 键会触发此事件。
		// 而 keydown 与 keyup，按下 Shift、Ctrl、Caps 都会触发，所以这里选择 keypress
		if ('' !== this.value) {
			// Yes. 只有 keypress 能正确获取到 e.charCode 的值
			// No. keydown、keyup 获取为 0
			// Yes. keydown、keypress、keyup 都能正确获取到 key
			const keyCode = e.keyCode || e.which || e.charCode
			if ('Enter' === e.key || 13 === keyCode) {
				this.blur()
				findEmployeesBy()
			}
		}
	})

	// 搜索按钮点击事件
	$(document).on('click', '#findA', function () {
		if ('' !== this.previousElementSibling.value) {
			findEmployeesBy()
		}
	})

	// 根据搜索条件查找员工
	function findEmployeesBy() {
		// 每次查找之前都初始化数据
		init()
		for (let i = 1; i < 10; i++) {
			thead.find('tr th:eq(' + i + ')').val('')
			thead.find('tr th:eq(' + i + ') i').attr('class', 'bi bi-chevron-expand')
		}
		$.ajax({
			type: 'POST',
			// 下面这行非常重要，没有会报错
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
			contentType: 'application/json',
			// 存储搜索记录并把搜索者传到后台
			url: '/employee/findEmployeesBy',
			data: JSON.stringify(new Employee()),
			// 因为开启了 csrf，所以增加请求头
			// 终于成功了！！！
			headers: {
				// 不区分大小写
				'X-CSRF-Token': token
			},
			success: function (data, success, state) {
				if (state.readyState === 4 && state.status === 200) {
					updatePage(data)
				}
			},
			error: function () {
				toastBody.text('查找失败，请检查后重试。')
				new bootstrap.Toast(liveToast).show()
			}
		})
	}

	// 根据排序条件查询
	// 写在这里方便查看
	let e = {
		name: 0,
		sex: 0,
		age: 0,
		idCard: 0,
		address: 0,
		phoneNumber: 0,
		createdBy: 0,
		createdDate: 0,
		lastModifiedDate: 0
	}

	function init() {
		e = {
			name: 0,
			sex: 0,
			age: 0,
			idCard: 0,
			address: 0,
			phoneNumber: 0,
			createdBy: 0,
			createdDate: 0,
			lastModifiedDate: 0
		}
	}

	$(document).on('click', 'thead tr th', function () {
		switch (this.cellIndex) {
			case 1:
				switch (e.name) {
					case 0:
						sortDirectionCondition('ASC', 'employeeName')
						$(this).find('i').attr('class', 'bi bi-chevron-up')
						$(this).val('ASC')
						init()
						e.name = 1
						break
					case 1:
						sortDirectionCondition('DESC', 'employeeName')
						$(this).find('i').attr('class', 'bi bi-chevron-down')
						$(this).val('DESC')
						init()
						e.name = 2
						break
					case 2:
						reset(this)
						init()
				}
				break
			case 2:
				switch (e.sex) {
					case 0:
						sortDirectionCondition('ASC', 'employeeSex')
						$(this).find('i').attr('class', 'bi bi-chevron-up')
						$(this).val('ASC')
						init()
						e.sex = 1
						break
					case 1:
						sortDirectionCondition('DESC', 'employeeSex')
						$(this).find('i').attr('class', 'bi bi-chevron-down')
						$(this).val('DESC')
						init()
						e.sex = 2
						break
					case 2:
						reset(this)
						init()
				}
				break
			case 3:
				switch (e.age) {
					case 0:
						sortDirectionCondition('ASC', 'employeeAge')
						$(this).find('i').attr('class', 'bi bi-chevron-up')
						$(this).val('ASC')
						init()
						e.age = 1
						break
					case 1:
						sortDirectionCondition('DESC', 'employeeAge')
						$(this).find('i').attr('class', 'bi bi-chevron-down')
						$(this).val('DESC')
						init()
						e.age = 2
						break
					case 2:
						reset(this)
						init()
				}
				break
			case 4:
				switch (e.idCard) {
					case 0:
						sortDirectionCondition('ASC', 'employeeIdCard')
						$(this).find('i').attr('class', 'bi bi-chevron-up')
						$(this).val('ASC')
						init()
						e.idCard = 1
						break
					case 1:
						sortDirectionCondition('DESC', 'employeeIdCard')
						$(this).find('i').attr('class', 'bi bi-chevron-down')
						$(this).val('DESC')
						init()
						e.idCard = 2
						break
					case 2:
						reset(this)
						init()
				}
				break
			case 5:
				switch (e.address) {
					case 0:
						sortDirectionCondition('ASC', 'employeeAddress')
						$(this).find('i').attr('class', 'bi bi-chevron-up')
						$(this).val('ASC')
						init()
						e.address = 1
						break
					case 1:
						sortDirectionCondition('DESC', 'employeeAddress')
						$(this).find('i').attr('class', 'bi bi-chevron-down')
						$(this).val('DESC')
						init()
						e.address = 2
						break
					case 2:
						reset(this)
						init()
				}
				break
			case 6:
				switch (e.phoneNumber) {
					case 0:
						sortDirectionCondition('ASC', 'employeePhoneNumber')
						$(this).find('i').attr('class', 'bi bi-chevron-up')
						$(this).val('ASC')
						init()
						e.phoneNumber = 1
						break
					case 1:
						sortDirectionCondition('DESC', 'employeePhoneNumber')
						$(this).find('i').attr('class', 'bi bi-chevron-down')
						$(this).val('DESC')
						init()
						e.phoneNumber = 2
						break
					case 2:
						reset(this)
						init()
				}
				break
			case 7:
				switch (e.createdBy) {
					case 0:
						sortDirectionCondition('ASC', 'createdBy')
						$(this).find('i').attr('class', 'bi bi-chevron-up')
						$(this).val('ASC')
						init()
						e.createdBy = 1
						break
					case 1:
						sortDirectionCondition('DESC', 'createdBy')
						$(this).find('i').attr('class', 'bi bi-chevron-down')
						$(this).val('DESC')
						init()
						e.createdBy = 2
						break
					case 2:
						reset(this)
						init()
				}
				break
			case 8:
				switch (e.createdDate) {
					case 0:
						sortDirectionCondition('ASC', 'createdDate')
						$(this).find('i').attr('class', 'bi bi-chevron-up')
						$(this).val('ASC')
						init()
						e.createdDate = 1
						break
					case 1:
						sortDirectionCondition('DESC', 'createdDate')
						$(this).find('i').attr('class', 'bi bi-chevron-down')
						$(this).val('DESC')
						init()
						e.createdDate = 2
						break
					case 2:
						reset(this)
						init()
				}
				break
			case 9:
				switch (e.lastModifiedDate) {
					case 0:
						sortDirectionCondition('ASC', 'lastModifiedDate')
						$(this).find('i').attr('class', 'bi bi-chevron-up')
						$(this).val('ASC')
						init()
						e.lastModifiedDate = 1
						break
					case 1:
						sortDirectionCondition('DESC', 'lastModifiedDate')
						$(this).find('i').attr('class', 'bi bi-chevron-down')
						$(this).val('DESC')
						init()
						e.lastModifiedDate = 2
						break
					case 2:
						reset(this)
						init()
				}
		}

		// 使第三次点击回到初始状态
		function reset($this) {
			sortDirectionCondition('ASC', 'createdDate')
			$($this).find('i').attr('class', 'bi bi-chevron-expand')
			$($this).val('')
		}

		// 删除其他 thead tr th 的 val()
		const ths = []
		// 只选取支持排序功能的 th
		for (let i = 1; i < 10; i++) {
			ths.push(thead.find('tr th:eq(' + i + ')'))
		}
		// 不包括自身 this.cellIndex - 1
		ths.splice(this.cellIndex - 1, 1)
		// 另外一种写法
		/*ths = $.grep(ths, function (n, i) {
			return i !== cellIndex - 1
		})*/
		// Delete th's val() & change i's class
		for (let th of ths) {
			th.find('i').attr('class', 'bi bi-chevron-expand')
			th.val('')
		}
		/*for (let i = 0 i < ths.length i++) {
			ths[i].val('')
		}*/
	})

	// 执行指定的排序查询————有查询条件，或者当只有一页数据并添加数据时会通过这里查询
	function sortDirectionCondition(direction, property) {
		$.ajax({
			type: 'POST',
			// 下面这行非常重要，没有会报错
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
			contentType: 'application/json',
			url: '/employee/findEmployeesBy?direction=' + direction + '&property=' + property,
			data: JSON.stringify(new Employee()),
			// 因为开启了 csrf，所以增加请求头
			headers: {
				'X-CSRF-Token': token
			},
			success: function (data, success, state) {
				if (state.readyState === 4 && state.status === 200) {
					updatePage(data)
				}
			},
			error: function () {
				toastBody.text('页面加载失败，请检查后重试。')
				new bootstrap.Toast(liveToast).show()
			}
		})
	}

	// 首页、上一页、中间页、下一页、尾页
	$(document).on('click', '.page-link', function () {
		const strings = getDirectionAndProperty().split(', ')
		ajaxRequestCondition(this.name, strings[0], strings[1])
	})

	// ajax - 有查询条件
	function ajaxRequestCondition(pageNum, direction, property) {
		if (pageNum > -1) {
			$.ajax({
				type: 'POST',
				// 下面这行非常重要，没有会报错
				// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
				// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
				contentType: 'application/json',
				url: '/employee/findEmployeesBy?pageNum=' + pageNum + '&direction=' + direction + '&property=' + property,
				data: JSON.stringify(new Employee()),
				// 因为开启了 csrf，所以增加请求头
				headers: {
					'X-CSRF-Token': token
				},
				success: function (data, success, state) {
					if (state.readyState === 4 && state.status === 200) {
						if (/updateEmployee/.test(data)) {
							updatePage(data)
						} else {
							// 如果该页（最后一页）没有数据时，将会向前面一页查询数据
							if (pageNum > 0) {
								$.ajax({
									type: 'POST',
									// 下面这行非常重要，没有会报错
									// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
									// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
									contentType: 'application/json',
									url: '/employee/findEmployeesBy?pageNum=' + (pageNum - 1) + '&direction=' + direction + '&property=' + property,
									data: JSON.stringify(new Employee()),
									// 因为开启了 csrf，所以增加请求头
									headers: {
										'X-CSRF-Token': token
									},
									success: function (data, success, state) {
										if (state.readyState === 4 && state.status === 200) {
											updatePage(data)
										}
									},
									error: function () {
										toastBody.text('页面加载失败，请检查后重试。')
										new bootstrap.Toast(liveToast).show()
									}
								})
							} else {
								tbody.children().remove()
								tfoot.find('tr td div').children().remove()
							}
						}
					}
				},
				error: function () {
					toastBody.text('页面加载失败，请检查后重试。')
					new bootstrap.Toast(liveToast).show()
				}
			})
		} else {
			sortDirectionCondition(direction, property)
		}
	}
}()

window.onload = function () {

	'use strict'

	/*const listeners = {
		dark: (mediaQueryList) => {
			if (mediaQueryList.matches) {
				$("body").css("background", '#1B1B1B')
				// document.body.bgColor = '#1B1B1B'
			}
		},
		light: (mediaQueryList) => {
			if (mediaQueryList.matches) {
				$("body").css("background", '#FFFFFF')
				// document.body.bgColor = '#FFFFFF'
			}
		}
	}

	window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', listeners.dark)
	window.matchMedia('(prefers-color-scheme: light)').addEventListener('change', listeners.light)*/
}