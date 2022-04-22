(function ($, w, d, log) {
	// ʹ���Ͻ�ģʽ
	"use strict";

	let $jqConfirm = null;
	let target = null;

	$.fn.jqConfirm = function (callback, options) {
		// �ص�����
		callback = callback || function () {
		};

		// Ĭ�ϲ���
		const defaults = {
			confirmText: 'ȷ�ϴ˲�����',
			submitBtnText: 'ȷ��',
			cancelBtnText: 'ȡ��'
		};

		// �ϲ�����
		options = $.extend(defaults, options);

		// Ԫ��
		const jqConfirmId = "jqconfirm";
		const jqConfirmSubmitId = "jqconfirm-submit";
		const jqConfirmCancelId = "jqconfirm-cancel";
		const jqConfirmHtml = '<div id="jqconfirm">'
			+ '<div id="jqconfirm-text">' + options.confirmText + '</div>'
			+ '<div id="jqconfirm-options">'
			+ '<button id="jqconfirm-submit" type="button">' + options.submitBtnText + '</button>'
			+ '<button id="jqconfirm-cancel" type="button">' + options.cancelBtnText + '</button>'
			+ '</div>'
			+ '</div>';

		// ��������Ƿ����
		if (!$('#' + jqConfirmId).size()) {
			$(document.body).append(jqConfirmHtml);
		}

		// ȫ�ֱ���
		$jqConfirm = $('#' + jqConfirmId);

		// �رհ�ť
		const jqConfirmClose = function () {
			const jsOutHeight = $jqConfirm.outerHeight();
			$jqConfirm.offset();
			const css = {
				display: 'none'
			};
			const animate = {
				top: '+=' + jsOutHeight + 'px',
				opacity: 0
			};

			$jqConfirm.animate(animate, 300, function () {
				$jqConfirm.css(css);
			});
		};

		// ȷ����ť����¼�
		$(document).on('click', '#' + jqConfirmSubmitId, function () {
			if (typeof callback == 'function') {
				callback.call(target, $(this));
				jqConfirmClose();
			}
		});

		// ȡ����ť�¼�
		$(document).on('click', '#' + jqConfirmCancelId, jqConfirmClose);

		// ����Ԫ��
		return this.each(function () {
			$(this).on('click', function () {
				const $this = $(this);
				target = $this;

				const confirmText = $this.attr('confirmtext') || options.confirmText;
				const submitBtnText = $this.attr('submitbtntext') || options.submitBtnText;
				const cancelBtnText = $this.attr('cancelbtntext') || options.cancelBtnText;

				const jsOutWidth = $jqConfirm.outerWidth();
				const jsOutHeight = $jqConfirm.outerHeight();

				const aOutWidth = $this.outerWidth();
				const aOutHeight = $this.outerHeight();
				const aOffset = $this.offset();
				const css = {
					left: (aOffset.left + (aOutWidth - jsOutWidth) / 2),
					top: aOffset.top,
					display: 'block',
					opacity: 0
				};
				let animate_top = jsOutHeight - aOutHeight;
				// ����߶��������
				if (aOffset.top - animate_top < 0) {
					animate_top += aOffset.top - animate_top;
				}
				// log(aOffset.top, animate_top, aOffset.top-animate_top);
				const animate = {
					top: '-=' + animate_top + 'px',
					opacity: 1
				};

				$jqConfirm.find('#jqconfirm-text').html(confirmText).end()
					.find('#jqconfirm-submit').text(submitBtnText).end()
					.find('#jqconfirm-cancel').text(cancelBtnText).end()
					.css(css).animate(animate, 200);
			});
		});
	}
})(jQuery, window, document, function () {
	if (typeof console.log === 'undefined') return;
	console.log(arguments);
});