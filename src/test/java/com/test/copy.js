$(document).ready(function () {

	// Show employees' sex and age
	// Get the total number of employees
	const totalNumber = $('tbody tr').length;
	for (let i = 1; i <= totalNumber; i++) {
		// every employee's sex
		let sex = $('#sexSelectId' + i).attr('class');
		// every employee's age
		let age = $('#ageSelectId' + i).attr('class');
		// �����Ա�ʹ option ѡ��
		$('#sexSelectId' + i + " option[value='" + sex + "']").attr('selected', 'selected');
		// ��������ʹ option ѡ��
		$('#ageSelectId' + i + " option[value='" + age + "']").attr('selected', 'selected');
	}

	// ���Ա��
	// function (params)
	// html ��θ� params ���Σ�
	$('#saveEmployee').on('click', function () {

		// ��֤����
		// ��ȡ������ʱ���ƣ����´��룩����Ϊ���鲢����
		const employeeName = $('#recipient-name').val();
		if (!regExpEmployeeName.test(employeeName)) {
			alert('����ֻ֧���� 1 - 25 �����֡�Ӣ�ġ����֡��ո��?����ϡ�');
			return false;
		}

		// ��֤���֤
		let employeeIdCard = $('#recipient-idCard').val();
		if (employeeIdCard.length !== 15 && employeeIdCard.length !== 18) {
			alert('����д 15 ���� 18 λ���֤���롣');
			return false;
		}
		// 15��18 λ���֤�����һ����֤
		if (!regExpIdCard.test(employeeIdCard)) {
			alert('���֤�����ʽ������������ԡ�');
			return false;
		}
		// 18 λ���֤����ڶ�����֤
		if (employeeIdCard.length === 18) {
			let id17 = [];
			for (let i = 0, j = 17; i < 17, j > 0; i++, j--) {
				id17[i] = employeeIdCard.substring(i, 18 - j);
			}
			const lastNumber = calculateLastNumber(id17);
			employeeIdCard = employeeIdCard.toUpperCase();
			if (lastNumber !== employeeIdCard.substring(17)) {
				alert('���֤�����ʽ������������ԡ�');
				return false;
			}
		}

		// ��֤סַ
		const employeeAddress = $('#recipient-address').val();
		if (!regExpEmployeeAddress.test(employeeAddress)) {
			alert('סַֻ֧������� 45 �����֡�Ӣ�ġ��ո�Ӣ�Ķ��ź�?����ϡ�');
			return false;
		}

		// ��֤�绰���루��ʱֻ��֤��һ������µ��й���½�ƶ��ֻ����룩
		const employeePhoneNumber = $('#recipient-phoneNumber').val();
		if (!regExpEmployeePhoneNumber.test(employeePhoneNumber)) {
			alert('�绰�����ʽ������������ԡ�');
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
			// �������зǳ���Ҫ��û�лᱨ��
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
			contentType: 'application/json',
			url: '/employee/saveEmployee',
			data: JSON.stringify(new Employee()),
			success: function (data, success, state) {
				if (state.status === 200 && state.readyState === 4) {
					$('#save').blur();
					// test...
					console.log(data.valueOf());
					// undefined
					console.log(data.data);
					// console.log(data.success());
					console.log(data.toString());
					console.log(data.toLocaleString());
					// console.log(data.error());
					// undefined
					console.log(data.url);
					// undefined
					console.log(data.type);
					// undefined
					console.log(data.createdBy);
					console.log(data.constructor);
					console.log(success);
					console.log(state);
					console.log(state.readyState);
					console.log(state.responseText);
					console.log(state.status);
					console.log(state.statusText);
					console.log(state.state());
					console.log(state.data);
					console.log(state.constructor);
					console.log(state.contentType);
					console.log(state.employeeName);
					console.log(state.type);
					console.log(state.url);
					console.log(state.hasOwnProperty(state));
					console.log(state.isPrototypeOf(state));
					console.log(state.toLocaleString());
					console.log(state.propertyIsEnumerable(state));
					console.log(state.toString());
					console.log(state.valueOf());
				}
			},
			error: function () {
				alert('���ʧ�ܣ���������ԡ�');
			}
		})
	})

	// ����Ա��
	// ���Թ���������������д��һ���������������
	// ����¼�
	$('#findButton').on('click', function () {
		const param = $('#findInput').val();
		if (param !== '') {
			findEmployee(param);
			$('#findButton').blur();
		}
	})
	// �������������ݺ�Ļس��¼�
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

});

// ��������
const regExpEmployeeName = /^[\u4e00-\u9fa5\w\s?]{1,25}$/;
// ���֤����
const regExpIdCard = /^\d{15}|\d{18}|(\d{17}X|x)$/;
// סַ����
const regExpEmployeeAddress = /^[\u4e00-\u9fa5\w\s?,]{2,45}$/;
// �绰����������ʱֻ��֤��һ������µ��й���½�ƶ��ֻ����룩
const regExpEmployeePhoneNumber = /^1[3-9]\d{9}$/;

// ������������Ա��
function findEmployee(param) {

	const attribute = $('#findSelect').val();

	function Employee() {
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

	$.ajax({
		type: 'POST',
		// �������зǳ���Ҫ��û�лᱨ��
		// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
		// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
		contentType: 'application/json',
		url: '/employee/findEmployee',
		data: JSON.stringify(new Employee()),
		// xml ===      ֱ�� error
		// html ===     ����Ĭ�ϵ�����ҳ��� html��û�� tbody ����
		// script ===   Ҳû�� tbody ����
		// json ===     error
		// jsonp ===    error
		// text ===     ����ҳ��� html��û�� tbody ����
		// !!!!!!!!!!!!!!! ���ǲ��ҷ����������ض���Ҳ���԰��������ݵĵ�һҳ������ !!!!!!!!!!!!!!!
		// xml ===      error
		// html ===     �� tbody ����
		// script ===   �� tbody ����
		// json ===     error
		// jsonp ===    error
		// text ===     �� tbody ����
		// dataType: 'text',
		success: function (data, success, state) {
			if (state.status === 200 && state.readyState === 4) {
				// ��ȡ֮������ҳ��
				const array1 = data.split('<tbody>');
				const array2 = array1[1].split('</tbody>');
				const array3 = array2[1].split('<div class="modal-footer no-margin-top">');
				let array4 = array3[1].split('</div>', 1181);
				const regExp = /index/g;
				// �Ѷ���Ҳд��ҳ������·����
				array4 = array4[0].replace(regExp, 'findEmployee');
				// ��ȡ�ַ���֮����Ҫ����ת��Ϊ HTML
				// $('tbody').html($(array2[0]));
				$('tbody').html(array2[0]);
				$('.no-margin-top').html(array4);
			}
		},
		error: function () {
			alert('��ѯʧ�ܣ������ֶκ����ԡ�');
		}
	})

}

// ȷ��ɾ��?
function deleteEmployee(employeeId, input) {
	if (confirm('ȷ��ɾ��?')) {
		$.ajax({
			type: 'POST',
			// ��ǰ�����Ҫ��б��/ '/employee/deleteEmployee/'
			url: '/employee/deleteEmployee',
			data: {'employeeId': employeeId},
			success: function (data, success, state) {
				if (state.status === 200 && state.readyState === 4) {
					const tr = input.parentNode.parentNode;
					const tbody = tr.parentNode;
					tbody.removeChild(tr);
					// Get the total number of employees
					const totalNumber = $('tbody tr').length;
					for (let i = 0; i < totalNumber; i++) {
						$('tbody tr:eq(' + i + ') td:eq(0)').html(i + 1);
					}
				}
			},
			error: function () {
				alert('ɾ��ʧ�ܣ���������ԡ�');
			}
		})
	}
}

// ȷ������?
function updateEmployee(count, employeeId, createdDate) {
	// ��֤����
	// ��ȡ������ʱ���ƣ����´��룩����Ϊ���鲢����
	// th:oninput="value=value.replace(/[^\u4e00-\u9fa5\w\s?]/,'')"
	const employeeName = $('#trId' + count + ' td:eq(1) label input').val();
	if (!regExpEmployeeName.test(employeeName)) {
		alert('����ֻ֧���� 1 - 25 �����֡�Ӣ�ġ����֡��ո��?����ϡ�');
		return false;
	}
	// ��֤���֤
	const employeeIdCard = $('#trId' + count + ' td:eq(4) label input').val();
	if (employeeIdCard.length !== 15 && employeeIdCard.length !== 18) {
		alert('���֤�����ʽ������������ԡ�');
		return false;
	}
	// 15 λ���֤
	if (!regExpIdCard.test(employeeIdCard)) {
		alert('���֤�����ʽ������������ԡ�');
		return false;
	}
	// 18 λ���֤
	if (employeeIdCard.length === 18) {
		let id17 = [];
		for (let i = 0, j = employeeIdCard.length - 1; i < employeeIdCard.length - 1, j > 0; i++, j--) {
			id17[i] = employeeIdCard.substring(i, employeeIdCard.length - j);
		}
		const lastNumber = calculateLastNumber(id17);
		if (lastNumber !== employeeIdCard.substring(17)) {
			alert('���֤�����ʽ������������ԡ�');
			return false;
		}
	}
	// ��֤סַ
	const employeeAddress = $('#trId' + count + ' td:eq(5) label input').val();
	if (!regExpEmployeeAddress.test(employeeAddress)) {
		alert('סַֻ֧������� 45 �����֡�Ӣ�ġ��ո��?����ϡ�');
		return false;
	}
	// ��֤�绰���루��ʱֻ��֤��һ������µ��й���½�ƶ��ֻ����룩
	const employeePhoneNumber = $('#trId' + count + ' td:eq(6) label input').val();
	if (!regExpEmployeePhoneNumber.test(employeePhoneNumber)) {
		alert('�绰�����ʽ������������ԡ�');
		return false;
	}

	if (confirm('ȷ������?')) {
		const lastModifiedDate = new Date();
		const year = lastModifiedDate.getFullYear();
		const month = lastModifiedDate.getMonth() + 1 < 10 ? '0' + (lastModifiedDate.getMonth() + 1) : lastModifiedDate.getMonth() + 1;
		const date = lastModifiedDate.getDate() < 10 ? '0' + lastModifiedDate.getDate() : lastModifiedDate.getDate();
		const hours = lastModifiedDate.getHours() < 10 ? '0' + lastModifiedDate.getHours() : lastModifiedDate.getHours();
		const minutes = lastModifiedDate.getMinutes() < 10 ? '0' + lastModifiedDate.getMinutes() : lastModifiedDate.getMinutes();
		const seconds = lastModifiedDate.getSeconds() < 10 ? '0' + lastModifiedDate.getSeconds() : lastModifiedDate.getSeconds();

		function Employee() {
			this.employeeId = employeeId;
			this.employeeName = employeeName;
			this.employeeSex = $('#trId' + count + ' td:eq(2) label select').val();
			this.employeeAge = $('#trId' + count + ' td:eq(3) label select').val();
			this.employeeIdCard = employeeIdCard;
			this.employeeAddress = employeeAddress;
			this.employeePhoneNumber = employeePhoneNumber;
			this.createdBy = $('#trId' + count + ' td:eq(7)').text();
			this.createdDate = createdDate;
			this.lastModifiedDate = lastModifiedDate;
		}

		$.ajax({
			type: 'POST',
			// �������зǳ���Ҫ��û�лᱨ��
			// Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
			// Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported]
			contentType: 'application/json',
			url: '/employee/updateEmployee',
			data: JSON.stringify(new Employee()),
			success: function (data, success, state) {
				if (state.status === 200 && state.readyState === 4) {
					$('#trId' + count + ' td:eq(9)').html(year + '-' + month + '-' + date + ' ' + hours + ':' + minutes + ':' + seconds);
				}
			},
			error: function () {
				alert('����ʧ�ܣ���������ԡ�');
			}
		})
	}

}

// �������ṩ���֤��ǰ 17 λ������һλ
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