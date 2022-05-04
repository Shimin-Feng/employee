!function () {

	'use strict'

	// 通过输入框翻页的 enter 和 blur event
	$(document).on('keypress blur', 'span label input', function (e) {
		// keypress 相对于 keydown 与 keyup，只有按下 Enter 键会触发此事件。
		// 而 keydown 与 keyup，按下 Shift、Ctrl、Caps 都会触发，所以这里选择 keypress
		if ('' !== this.value) {
			// Yes. 只有 keypress 能正确获取到 e.charCode 的值
			// No. keydown、keyup 获取为 0
			// Yes. keydown、keypress、keyup 都能正确获取到 key
			const keyCode = e.keyCode || e.which || e.charCode
			if ('Enter' === e.key || 13 === keyCode || !$(this).is(':focus')) {
				if (0 === this.value % 1 && 0 < this.value && parseInt($('.page-link:last')[0].name) + 2 > this.value) {
					sendPage(this.value - 1)
					// 发送请求后将 this.value 的值设为 ''，以避免失焦后再次发送请求
					this.value = ''
				} else {
					$('.toast-body').text('请输入正确的页数。')
					new bootstrap.Toast($('#liveToast')).show()
				}
			}
		}
	})

	// 首页、上一页、中间页、下一页、尾页
	$(document).on('click', '.page-link', function () {
		sendPage(this.name)
	})

	function sendPage(page) {
		$.ajax({
			type: 'POST',
			url: '/findOperationLogsBy?pageNum=' + page,
			// 因为开启了 csrf，所以增加请求头
			headers: {
				'X-CSRF-Token': $('input:hidden[name="_csrf"]').val()
			},
			success: function (data, success, state) {
				if (state.readyState === 4 && state.status === 200) {
					// 更新页面数据
					if (/\d{11}/.test(data)) {
						// 截取之后填充进页面
						const trs = data.split('<tbody class="table-secondary">')[1].split('</tbody>'),
							ul = trs[1].split('<div class="modal-footer no-margin-top">')[1].split('</div>')
						$('tbody').html(trs[0])
						$('.no-margin-top').html(ul[0])
					} else {
						$('tbody').children().remove()
						$('tfoot').find('tr td div').children().remove()
					}
				}
			},
			error: function (state, error, data) {
				console.log(state)
				console.log(state.readyState)
				console.log(state.status)
				console.log(error)
				console.log(data)
				new bootstrap.Toast($('#liveToast')).show()
			}
		})
	}

}()