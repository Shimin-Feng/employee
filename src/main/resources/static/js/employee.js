!function () {

	'use strict'

	const thead = $('thead')
	const tbody = $('tbody')
	const tfoot = $('tfoot')
	const username = $('#username')
	const modalBody = $('.modal-body')
	const findSelect = $('#findSelect')
	const findInput = $('#findInput')
	const toastBody = $('.toast-body')
	const liveToast = $('#liveToast')

	// Get token
	const token = $("input:hidden[name='_csrf']").val()

	// 根据传参判断对象属性
	function Employee() {
		// MySQL 查询默认忽略大小写，所以 findInput.val() 不用转大写
		this[findSelect.val()] = findInput.val()
	}

	// 根据所提供身份证的前 17 位算出最后一位
	function getLastIdCardNumber(id17) {
		const weight = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]
		const validate = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2']
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
			const trs = data.split('<tbody class="table-secondary">')[1].split('</tbody>')
			const ul = trs[1].split('<div class="modal-footer no-margin-top">')[1].split('</div>')
			tbody.html(trs[0])
			$('.no-margin-top').html(ul[0])
		} else {
			tbody.children().remove()
			tfoot.find('tr td div').children().remove()
		}
	}

	// 验证信息
	function regExp(employeeName, employeeIdCard, employeeAddress, employeePhoneNumber) {
		// 验证姓名
		if (employeeName === '') {
			toastBody.text('姓名不能为空。')
			new bootstrap.Toast(liveToast).show()
			return false
		}
		if (!/^[\u4e00-\u9fa5\w\s•]{1,25}$/.test(employeeName)) {
			toastBody.text('姓名只支持由 1 - 25 个汉字、英文、数字、空格和•的组合。')
			new bootstrap.Toast(liveToast).show()
			return false
		}

		// 验证身份证号码
		if (employeeIdCard === '') {
			toastBody.text('身份证号码不能为空。')
			new bootstrap.Toast(liveToast).show()
			return false
		}
		if (employeeIdCard.length !== 15 && employeeIdCard.length !== 18) {
			toastBody.text('请填写 15 或者 18 位身份证号码。')
			new bootstrap.Toast(liveToast).show()
			return false
		}
		// 15、18 位身份证号码第一重验证
		if (!/^\d{15}|\d{18}|(\d{17}X|x)$/.test(employeeIdCard)) {
			toastBody.text('身份证号码有误，请检查后重试。')
			new bootstrap.Toast(liveToast).show()
			return false
		}
		// 18 位身份证号码第二重验证
		if (employeeIdCard.length === 18) {
			const idCard = employeeIdCard.split('')
			idCard.pop()
			if (employeeIdCard.toUpperCase().substring(17) !== getLastIdCardNumber(idCard)) {
				toastBody.text('身份证号码有误，请检查后重试。')
				new bootstrap.Toast(liveToast).show()
				return false
			}
		}

		// 验证住址
		if (employeeAddress === '') {
			toastBody.text('住址不能为空。')
			new bootstrap.Toast(liveToast).show()
			return false
		}
		if (!/^[\u4e00-\u9fa5\w\s•,]{2,45}$/.test(employeeAddress)) {
			toastBody.text('住址只支持由最多 45 个汉字、英文、空格、英文逗号和•的组合。')
			new bootstrap.Toast(liveToast).show()
			return false
		}

		// 验证电话号码（暂时只验证在一般情况下的中国大陆移动手机号码）
		if (employeePhoneNumber === '') {
			toastBody.text('电话号码不能为空。')
			new bootstrap.Toast(liveToast).show()
			return false
		}
		if (!/^1[3-9]\d{9}$/.test(employeePhoneNumber)) {
			toastBody.text('电话号码格式有误，请检查后重试。')
			new bootstrap.Toast(liveToast).show()
			return false
		}
	}

	// 若有则获取上一次的排序规则
	function getDirectionAndProperty() {
		let direction = 'ASC'
		let property = 'createdDate'
		// 只选取支持排序功能的 th
		for (let i = 1; i < 10; i++) {
			if (/^(ASC|DESC)$/.test(thead.find('tr th:eq(' + i + ')').val())) {
				direction = thead.find('tr th:eq(' + i + ')').val()
				if (i === 1) {
					property = 'employeeName'
					break
				} else if (i === 2) {
					property = 'employeeSex'
					break
				} else if (i === 3) {
					property = 'employeeAge'
					break
				} else if (i === 4) {
					property = 'employeeIdCard'
					break
				} else if (i === 5) {
					property = 'employeeAddress'
					break
				} else if (i === 6) {
					property = 'employeePhoneNumber'
					break
				} else if (i === 7) {
					property = 'createdBy'
					break
				} else if (i === 8) {
					break
				} else {
					property = 'lastModifiedDate'
					break
				}
			}
		}
		return direction + ', ' + property
	}

	// 添加、修改员工信息
	$(document).on('click', '#saveOrUpdateEmployee', function () {
		// 获取表单数据
		let arr = []
		$('.form-control').each(function () {
			arr.push($(this).val())
		})
		// 删除第一个不需要的值
		arr.shift()
		// 验证值
		if (regExp(arr[0], arr[1], arr[2], arr[3]) === false) {
			return false
		}
		const birth = arr[1].substring(6, 14)
		const birthYear = birth.substring(0, 4)
		const birthMonth = birth.substring(4, 6)
		const birthDay = birth.substring(6, 8)
		const createdDate = new Date()
		// 'PRC'(People Republic of China): 不区分大小写. '2-digit': 如果为 10 以下则在前面加 0. 只有 month, day 需为 '2-digit'
		const options = {
			timeZone: 'PRC',
			year: 'numeric', month: '2-digit', day: '2-digit',
			hour: 'numeric', minute: 'numeric', second: 'numeric'
		}
		// locales: 不区分大小写.
		const now = createdDate.toLocaleString('zh-CN', options)
		// 2012/02/01 01:01:01
		const nowYear = now.substring(0, 4)
		const nowMonth = now.substring(5, 7)
		const nowDay = now.substring(8, 10)

		function Employee() {
			this['employeeName'] = arr[0]
			// 取消人为操作性别和年龄
			// 改为程序根据身份证判断性别和年龄
			this['employeeSex'] = arr[1].substring(16, 17) % 2 === 0 ? '女' : '男'
			this['employeeAge'] = nowDay - birthDay < 0
				? nowMonth - 1 - birthMonth < 0 ? nowYear - 1 - birthYear : nowYear - birthYear
				: nowMonth - birthMonth < 0 ? nowYear - 1 - birthYear : nowYear - birthYear
			this['employeeIdCard'] = arr[1].toUpperCase()
			this['employeeAddress'] = arr[2]
			this['employeePhoneNumber'] = arr[3]
			this['lastModifiedDate'] = createdDate.toLocaleString('zh-CN', options)
			// 添加员工
			if (modalBody.val() === undefined || modalBody.val() === '' || modalBody.val() === null) {
				this['createdBy'] = username.text()
				this['createdDate'] = createdDate.toLocaleString('zh-CN', options)
			} else {
				// 修改员工
				this['employeeId'] = tbody.find('tr:eq(' + modalBody.val() + ') th').attr('id')
				this['createdBy'] = tbody.find('tr:eq(' + modalBody.val() + ') td:eq(6)').text()
				this['createdDate'] = tbody.find('tr:eq(' + modalBody.val() + ') td:eq(7)').text()
				modalBody.val('')
			}
		}

		$.ajax({
			type: 'POST',
			// 下面这行非常重要，没有会报错
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencodedcharset=UTF-8' not supported]
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
		if (employeeId === undefined || employeeId === '' || employeeId === null) {
			toastBody.text('没有获取到员工 ID，请检查后重试。')
			new bootstrap.Toast(liveToast).show()
			return false
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
		if (findInput.val() === '') {
			ajaxRequest($('.currentPage').attr('name'), strings[0], strings[1])
		} else {
			ajaxRequestCondition($('.currentPage').attr('name'), strings[0], strings[1])
		}
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
		minLength: 0,
		source: function (request, response) {
			$.ajax({
				type: 'POST',
				url: '/findRecordNamesBy',
				data: {
					'username': username.text(),
					'searchGroupBy': findSelect.val(),
					'recordName': request.term
				},
				dataType: "json",
				headers: {
					// 不区分大小写
					'X-CSRF-Token': token
				},
				success: function (data, success, state) {
					if (state.readyState === 4 && state.status === 200) {
						response(data)
						$('#ui-id-2').prepend('<div class="delete-record-nane" style="background-color: #ff3300">test-before</div>')
							.append('<div class="delete-record-nane" style="background-color: #ff3300">test-after</div>')
					}
				},
				error: function (state, error, data) {
					console.log(state)
					console.log(state.readyState)
					console.log(state.status)
					console.log(error)
					console.log(data)
					findInput.autocomplete("close")
				}
			})
		},
		select: function (event, ui) {
			$(this).val(ui.item.label)
			this.blur()
			findEmployee()
			return false
		}
	}).focus(function () {
		$(this).autocomplete("search", $(this).val())
	})

	// 查找员工
	// 尝试过把下面两个方法写进一个方法里，但不理想
	// 键盘回车事件
	$(document).on({
		/*focus: function () {
			console.log('focus ------------------------')
			$.ajax({
				data: {
					'username': username.text(),
					'searchGroupBy': findSelect.val(),
				},
				dataType: 'json',
				headers: {
					'X-CSRF-Token': token
				},
				type: 'POST',
				url: '/findRecordNamesBy',
				success: function (data, success, state) {
					if (state.readyState === 4 && state.status === 200) {
						console.log(data)
						findInput.autocomplete({
							minLength: 0,
							source: data,
							create: function (event, ui) {
								console.log('create--------------------')
								console.log(event)
								console.log(ui)
								$(this).autocomplete({minLength: 0, source: data}).focus(function () {
									console.log(222)
									$(this).autocomplete("search", $(this).val())
								})
							}
						}).focus(function () {
							console.log(1111)
							$(this).autocomplete("search", $(this).val())
						})
						return
						const suggestWrap = $('#search_suggest')
						if (data.length < 1) {
							suggestWrap.hide()
							return
						}
						let li
						const tmpFrag = document.createDocumentFragment()
						suggestWrap.find('ul').html('')
						for (let i = 0 i < data.length i++) {
							li = document.createElement('li')
							li.innerHTML = data[i]
							tmpFrag.appendChild(li)
						}
						suggestWrap.find('ul').append(tmpFrag)
						suggestWrap.show()
					}
				},
				error: function (state, error, data) {
					console.log(state)
					console.log(state.readyState)
					console.log(state.status)
					console.log(error)
					console.log(data)
				}
			})
		},*/
		keyup: function (e) {
			if (this.value !== '') {
				const keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode
				if (keyCode === 13) {
					this.blur()
					findEmployee()
				}
			}
		}
	}, '#findInput')

	// 搜索按钮点击事件
	$(document).on('click', '#findA', function () {
		if (this.previousElementSibling.value !== '') {
			findEmployee()
		}
	})

	// 根据搜索条件查找员工
	function findEmployee() {
		// 每次查找之前都重置数据
		init()
		for (let i = 1; i < 10; i++) {
			thead.find('tr th:eq(' + i + ')').val('')
			thead.find('tr th:eq(' + i + ') i').attr('class', 'bi bi-chevron-expand')
		}
		$.ajax({
			type: 'POST',
			// 下面这行非常重要，没有会报错
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencodedcharset=UTF-8' not supported]
			contentType: 'application/json',
			// 存储搜索记录并把搜索者传到后台
			url: '/employee/findEmployeesBy?username=' + username.text(),
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
	let employeeName = 0
	let employeeSex = 0
	let employeeAge = 0
	let employeeIdCard = 0
	let employeeAddress = 0
	let employeePhoneNumber = 0
	let createdBy = 0
	let createdDate = 0
	let lastModifiedDate = 0

	function init() {
		employeeName = 0
		employeeSex = 0
		employeeAge = 0
		employeeIdCard = 0
		employeeAddress = 0
		employeePhoneNumber = 0
		createdBy = 0
		createdDate = 0
		lastModifiedDate = 0
	}

	$(document).on('click', 'thead tr th', function () {
		if (this.cellIndex === 1) {
			if (employeeName === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'employeeName')
				} else {
					sortDirectionCondition('ASC', 'employeeName')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-up')
				$(this).val('ASC')
				init()
				employeeName = 1
			} else if (employeeName === 1) {
				if (findInput.val() === '') {
					sortDirection('DESC', 'employeeName')
				} else {
					sortDirectionCondition('DESC', 'employeeName')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-down')
				$(this).val('DESC')
				init()
				employeeName = 2
			} else {
				reset(this)
				init()
			}
		} else if (this.cellIndex === 2) {
			if (employeeSex === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'employeeSex')
				} else {
					sortDirectionCondition('ASC', 'employeeSex')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-up')
				$(this).val('ASC')
				init()
				employeeSex = 1
			} else if (employeeSex === 1) {
				if (findInput.val() === '') {
					sortDirection('DESC', 'employeeSex')
				} else {
					sortDirectionCondition('DESC', 'employeeSex')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-down')
				$(this).val('DESC')
				init()
				employeeSex = 2
			} else {
				reset(this)
				init()
			}
		} else if (this.cellIndex === 3) {
			if (employeeAge === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'employeeAge')
				} else {
					sortDirectionCondition('ASC', 'employeeAge')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-up')
				$(this).val('ASC')
				init()
				employeeAge = 1
			} else if (employeeAge === 1) {
				if (findInput.val() === '') {
					sortDirection('DESC', 'employeeAge')
				} else {
					sortDirectionCondition('DESC', 'employeeAge')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-down')
				$(this).val('DESC')
				init()
				employeeAge = 2
			} else {
				reset(this)
				init()
			}
		} else if (this.cellIndex === 4) {
			if (employeeIdCard === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'employeeIdCard')
				} else {
					sortDirectionCondition('ASC', 'employeeIdCard')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-up')
				$(this).val('ASC')
				init()
				employeeIdCard = 1
			} else if (employeeIdCard === 1) {
				if (findInput.val() === '') {
					sortDirection('DESC', 'employeeIdCard')
				} else {
					sortDirectionCondition('DESC', 'employeeIdCard')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-down')
				$(this).val('DESC')
				init()
				employeeIdCard = 2
			} else {
				reset(this)
				init()
			}
		} else if (this.cellIndex === 5) {
			if (employeeAddress === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'employeeAddress')
				} else {
					sortDirectionCondition('ASC', 'employeeAddress')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-up')
				$(this).val('ASC')
				init()
				employeeAddress = 1
			} else if (employeeAddress === 1) {
				if (findInput.val() === '') {
					sortDirection('DESC', 'employeeAddress')
				} else {
					sortDirectionCondition('DESC', 'employeeAddress')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-down')
				$(this).val('DESC')
				init()
				employeeAddress = 2
			} else {
				reset(this)
				init()
			}
		} else if (this.cellIndex === 6) {
			if (employeePhoneNumber === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'employeePhoneNumber')
				} else {
					sortDirectionCondition('ASC', 'employeePhoneNumber')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-up')
				$(this).val('ASC')
				init()
				employeePhoneNumber = 1
			} else if (employeePhoneNumber === 1) {
				if (findInput.val() === '') {
					sortDirection('DESC', 'employeePhoneNumber')
				} else {
					sortDirectionCondition('DESC', 'employeePhoneNumber')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-down')
				$(this).val('DESC')
				init()
				employeePhoneNumber = 2
			} else {
				reset(this)
				init()
			}
		} else if (this.cellIndex === 7) {
			if (createdBy === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'createdBy')
				} else {
					sortDirectionCondition('ASC', 'createdBy')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-up')
				$(this).val('ASC')
				init()
				createdBy = 1
			} else if (createdBy === 1) {
				if (findInput.val() === '') {
					sortDirection('DESC', 'createdBy')
				} else {
					sortDirectionCondition('DESC', 'createdBy')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-down')
				$(this).val('DESC')
				init()
				createdBy = 2
			} else {
				reset(this)
				init()
			}
		} else if (this.cellIndex === 8) {
			if (createdDate === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'createdDate')
				} else {
					sortDirectionCondition('ASC', 'createdDate')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-up')
				$(this).val('ASC')
				init()
				createdDate = 1
			} else if (createdDate === 1) {
				if (findInput.val() === '') {
					sortDirection('DESC', 'createdDate')
				} else {
					sortDirectionCondition('DESC', 'createdDate')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-down')
				$(this).val('DESC')
				init()
				createdDate = 2
			} else {
				reset(this)
				init()
			}
		} else if (this.cellIndex === 9) {
			if (lastModifiedDate === 0) {
				if (findInput.val() === '') {
					sortDirection('ASC', 'lastModifiedDate')
				} else {
					sortDirectionCondition('ASC', 'lastModifiedDate')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-up')
				$(this).val('ASC')
				init()
				lastModifiedDate = 1
			} else if (lastModifiedDate === 1) {
				if (findInput.val() === '') {
					sortDirection('DESC', 'lastModifiedDate')
				} else {
					sortDirectionCondition('DESC', 'lastModifiedDate')
				}
				$(this).find('i').attr('class', 'bi bi-chevron-down')
				$(this).val('DESC')
				init()
				lastModifiedDate = 2
			} else {
				reset(this)
				init()
			}
		}

		function reset($this) {
			if (findInput.val() === '') {
				sortDirection('ASC', 'createdDate')
			} else {
				sortDirectionCondition('ASC', 'createdDate')
			}
			$($this).find('i').attr('class', 'bi bi-chevron-expand')
			$($this).val('')
		}

		// 删除其他 thead tr th 的 val()
		let ths = []
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

	// 执行指定的排序查询————无查询条件
	function sortDirection(direction, property) {
		$.ajax({
			type: 'POST',
			// 一定要记得 `=` 符号!
			url: '/employee?direction=' + direction + '&property=' + property,
			// 因为开启了 csrf，所以增加请求头
			headers: {
				'X-CSRF-Token': token
			},
			success: function (data, success, state) {
				if (state.readyState === 4 && state.status === 200) {
					updatePage(data)
				}
			}
		})
	}

	// 执行指定的排序查询————有查询条件，或者当只有一页数据并添加数据时会通过这里查询
	function sortDirectionCondition(direction, property) {
		$.ajax({
			type: 'POST',
			// 下面这行非常重要，没有会报错
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencodedcharset=UTF-8' not supported]
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
			}
		})
	}

	// 首页、上一页、中间页、下一页、尾页
	$(document).on('click', '.page-link', function () {
		const strings = getDirectionAndProperty().split(', ')
		if (findInput.val() === '') {
			ajaxRequest(this.name, strings[0], strings[1])
		} else {
			ajaxRequestCondition(this.name, strings[0], strings[1])
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
					'X-CSRF-Token': token
				},
				success: function (data, success, state) {
					if (state.readyState === 4 && state.status === 200) {
						// 如果当前页还有数据
						if (/updateEmployee/.test(data)) {
							updatePage(data)
						} else {
							if (pageNum > 0) {
								$.ajax({
									type: 'POST',
									url: '/employee?pageNum=' + (pageNum - 1) + '&direction=' + direction + '&property=' + property,
									// 因为开启了 csrf，所以增加请求头
									headers: {
										'X-CSRF-Token': token
									},
									success: function (data, success, state) {
										if (state.readyState === 4 && state.status === 200) {
											updatePage(data)
										}
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
			sortDirection(direction, property)
		}
	}

	// ajax - 有查询条件
	function ajaxRequestCondition(pageNum, direction, property) {
		if (pageNum > -1) {
			$.ajax({
				type: 'POST',
				// 下面这行非常重要，没有会报错
				// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
				// Content type 'application/x-www-form-urlencodedcharset=UTF-8' not supported]
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
							if (pageNum > 0) {
								$.ajax({
									type: 'POST',
									// 下面这行非常重要，没有会报错
									// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
									// Content type 'application/x-www-form-urlencodedcharset=UTF-8' not supported]
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

	/*debugger
	// const availableTags = []
	$.ajax({
		type: 'POST',
		url: '/employee/findAllSearchRecords',
		headers: {
			'X-CSRF-Token': $("input:hidden[name='_csrf']").val()
		},
		success: function (data, success, state) {
			if (state.readyState === 4 && state.status === 200) {
				console.log(data)
				console.log(success)
				console.log(state)
			}
		},
		error: function () {
			new bootstrap.Toast(liveToast).show()
		}
	})*/

	/*let listeners = {
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