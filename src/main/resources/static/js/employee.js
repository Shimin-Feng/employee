(() => {

    'use strict'

    /**
     * 考虑到性能和效率问题，这里选择 getElement 系列，为不是 querySelector 系列选择器
     * 并且 getElement 系列选择器获取的数组结果为动态，querySelector 为静态
     *
     * @type {HTMLCollectionOf<HTMLElementTagNameMap[string]>}
     */
    const theadTrThCollectionOf = document.getElementsByTagName('thead')[0].getElementsByTagName('tr')[0].getElementsByTagName('th'),
        /**
         * @type {HTMLTableSectionElement}
         */
        tbody = document.getElementsByTagName('tbody')[0],
        /**
         * @type {HTMLCollectionOf<HTMLElementTagNameMap[string]>}
         */
        tbodyTrCollectionOf = tbody.getElementsByTagName('tr'),
        /**
         * @type {HTMLTableSectionElement}
         */
        tfoot = document.getElementsByTagName('tfoot')[0],
        /**
         * @type {HTMLDivElement}
         */
        tfootTrTdDiv = tfoot.getElementsByTagName('tr')[0].getElementsByTagName('td')[0].getElementsByTagName('div')[0],
        /**
         * @type {Element}
         */
        modalBody = document.getElementsByClassName('modal-body')[0],
        /**
         * @type {Element}
         */
        modalFooter = document.getElementsByClassName('modal-footer')[1],
        /**
         * @type {HTMLDivElement}
         */
        sectionDiv = document.getElementsByTagName('section')[0].getElementsByTagName('div')[0],
        /**
         * @type {HTMLSelectElement}
         */
        sectionDivSelect = sectionDiv.getElementsByTagName('select')[0],
        /**
         * @type {HTMLInputElement}
         */
        sectionDivInput = sectionDiv.getElementsByTagName('input')[0],
        /**
         * @type {Element}
         */
        toastBody = document.getElementsByClassName('toast-body')[0],
        /**
         * @type {HTMLElement}
         */
        liveToast = document.getElementById('liveToast'),
        /**
         * Get token
         * @type {String}
         */
        token = document.getElementsByName('_csrf')[0].value
    ;

    /**
     * 根据所提供身份证的前 17 位算出最后一位
     * @param id17 18 位身份证前面的 17 位数字
     * @returns {string} 返回计算后的最后的一位验证码数字
     */
    function getLastIdCardNumber(id17) {
        const weight = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2],
            validate = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2']
        let sum = 0

        for (let i = 0; i < id17.length; i++)
            sum += id17[i] * weight[i]
        return validate[sum % 11]
    }

    /**
     * 更新页面数据
     * @param data html 页面数据
     */
    function updatePage(data) {
        if (/btn-primary/.test(data)) {
            // 截取之后填充进页面
            const trs = data.split('<tbody class="table-secondary">')[1].split('</tbody>'),
                ul = trs[1].split('<div class="modal-footer no-margin-top">')[1].split('</div>')

            tbody.innerHTML = trs[0]
            document.getElementsByClassName('no-margin-top')[0].innerHTML = ul[0]

        } else {
            tbody.innerHTML = ''
            tfootTrTdDiv.innerHTML = ''
        }
    }

    /**
     * 验证信息
     * @param valuesArray 需要验证的字段
     * @returns {boolean} false 验证不通过，数据格式不正确
     */
    function regExp(valuesArray) {
        let msg = ''

        for (let i in valuesArray)
            if ('' === valuesArray[i]) {
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

        if ('' === msg) {
            // 验证姓名
            if (!/^[\u4e00-\u9fa5\w\s•]{1,25}$/.test(valuesArray[0]))
                msg = '姓名只支持由 1 - 25 个汉字、英文、数字、空格和•的组合。'

            // 验证身份证号码，15、18 位身份证号码第一重验证
            else if (valuesArray[1].length !== 15 && valuesArray[1].length !== 18)
                msg = '请填写 15 或者 18 位身份证号码。'

            // 15、18 位身份证号码第二重验证
            else if (!/^\d{15}|\d{18}|(\d{17}X|x)$/.test(valuesArray[1]))
                msg = '身份证号码有误，请检查后重试。'

            // 18 位身份证号码第三重验证
            else if (valuesArray[1].length === 18 && valuesArray[1].charAt(17).toUpperCase() !== getLastIdCardNumber(valuesArray[1].substring(0, 17).split('')))
                msg = '身份证号码有误，请检查后重试。'

            // 验证住址
            else if (!/^[\u4e00-\u9fa5\w\s•,]{2,45}$/.test(valuesArray[2]))
                msg = '住址只支持由最多 45 个汉字、英文、空格、英文逗号和•的组合。'

            // 验证电话号码（暂时只验证在一般情况下的中国大陆移动手机号码）
            else if (!/^1[3-9]\d{9}$/.test(valuesArray[3]))
                msg = '电话号码格式有误，请检查后重试。'
        }

        if ('' !== msg) {
            toastBody.textContent = msg;
            new bootstrap.Toast(liveToast).show();
            return false;
        }
    }

    /**
     * 若有则获取上一次的排序规则
     * @returns {string} 返回排序字段和值
     */
    function getDirectionAndProperty() {
        let direction = '',
            property = ''

        // 只寻找支持排序功能的 9 个 th 之一的值
        for (let i = 1; i < 10; i++) {
            if (/^(ASC|DESC)$/.test(theadTrThCollectionOf[i].valueOf().value)) {
                direction = theadTrThCollectionOf[i].valueOf().value

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

    /**
     * 添加、修改员工信息
     */
    modalFooter.getElementsByTagName('a')[1].addEventListener('click', () => {
        // 获取表单数据
        const formControl = modalBody.getElementsByClassName('form-control'),
            valuesArray = []
        for (let collectionElement of formControl)
            valuesArray.push(collectionElement.value)

        // regExp data
        if (regExp(valuesArray) === false) return

        // 如果变量 xhr 在全局范围内使用，它会在 makeRequest() 函数中被相互覆盖，从而导致资源竞争。
        // 为了避免这个情况，请在包含 AJAX 函数的闭包中声明 xhr 变量。
        let xhr;

        // Old compatibility code, no longer needed.
        if (window.XMLHttpRequest) // Mozilla, Safari, IE7+ ...
            xhr = new XMLHttpRequest();
        else if (window.ActiveXObject) // IE 6 and older
            xhr = new ActiveXObject("Microsoft.XMLHTTP");

        let employeeId = ''

        // 根据实际实际情况设置请求方式
        let method;

        // if it's updating an employee, also need to get the employeeId
        if (/\d/.test(modalBody.valueOf().value)) {
            employeeId = tbodyTrCollectionOf[modalBody.valueOf().value].getElementsByTagName('th')[0].getAttribute('id');
            // delete the index
            modalBody.valueOf().value = '';
            method = 'PATCH';
        } else
            method = 'POST';

        // 使用 FormDataAPI 是最简单最快的，但缺点是收集的数据不能被字符串化。仅使用 AJAX 更复杂，但通常更灵活、更强大。
        xhr.open(
            method,
            'employee/saveOrUpdateEmployee?employeeName=' + valuesArray[0] + '&employeeIdCard=' + valuesArray[1] +
            '&employeeAddress=' + valuesArray[2] + '&employeePhoneNumber=' + valuesArray[3] + '&employeeId=' + employeeId
        );

        saveUpdateDeleteRequest(xhr);
    });

    /**
     * 将更改和删除按钮的 click 事件委托在 tbody 标签上
     */
    tbody.addEventListener('click', e => {
        // 兼容性处理
        const event = e || window.event,
            target = event.target || event.srcElement

        /**
         * 将需要更改的员工信息填充进 modal
         */
        if ('btn btn-primary' === target.getAttribute('class')) {
            // 获取当前 tbody tr 下标
            const index = (target.parentNode.parentNode.firstElementChild.textContent - 1) % 10,
                tds = tbodyTrCollectionOf[index].getElementsByTagName('td')

            // 保存下标到模态弹框中
            modalBody.valueOf().value = index

            // 将 table 中的数据填充进模态弹框
            document.getElementById('recipient-name').value = tds[0].textContent
            document.getElementById('recipient-idCard').value = tds[3].textContent
            document.getElementById('recipient-address').value = tds[4].textContent
            document.getElementById('recipient-phoneNumber').value = tds[5].textContent
        }

        /**
         * 删除员工信息
         */
        if ('btn btn-danger' === target.getAttribute('class')) {
            const employeeId = target.parentElement.parentElement.firstElementChild.id

            if (!/^[\dA-Fa-f]{8}-[\dA-Fa-f]{4}-[\dA-Fa-f]{4}-[\dA-Fa-f]{4}-[\dA-Fa-f]{12}$/.test(employeeId)) {
                toastBody.textContent = '无法获取到员工 ID，请检查后重试。'
                new bootstrap.Toast(liveToast).show()
                return
            }

            if (confirm('确定删除?')) {

                // 如果变量 xhr 在全局范围内使用，它会在 makeRequest() 函数中被相互覆盖，从而导致资源竞争。
                // 为了避免这个情况，请在包含 AJAX 函数的闭包中声明 xhr 变量。
                let xhr;

                // Old compatibility code, no longer needed.
                if (window.XMLHttpRequest) // Mozilla, Safari, IE7+ ...
                    xhr = new XMLHttpRequest();
                else if (window.ActiveXObject) // IE 6 and older
                    xhr = new ActiveXObject("Microsoft.XMLHTTP");

                xhr.open('DELETE', 'employee/deleteEmployeeById?employeeId=' + employeeId);

                saveUpdateDeleteRequest(xhr);
            }
        }
    });

    /**
     * 避免点击更改弹出了 modal 更改后却并没有提交使 tbody tr 下标继续留在 .modal-body
     * @type {Element[]}
     */
    const elements = [document.getElementsByClassName('modal-header')[0], modalFooter];
    elements.forEach(function (element) {
        element.getElementsByTagName('a')[0].addEventListener('click', () => {

            modalBody.valueOf().value = ''
        })
    });

    /**
     * 封装添加、修改、删除操作的 Ajax
     * @param xhr XMLHttpRequest
     */
    function saveUpdateDeleteRequest(xhr) {
        // 这似乎是默认设置？
        // 如果不设置响应头 Cache-Control: no-cache 浏览器将会把响应缓存下来而且再也无法重新提交请求。
        // 你也可以添加一个总是不同的 GET 参数，比如时间戳或者随机数
        xhr.setRequestHeader('Cache-Control', 'no-cache');

        // 因为开启了 csrf，所以增加请求头
        xhr.setRequestHeader('X-CSRF-Token', token);

        xhr.send();

        // callback
        xhr.addEventListener('readystatechange', () => {

            // 在通信错误的事件中（例如服务器宕机），在访问响应状态 onreadystatechange 方法中会抛出一个例外。
            // 为了缓和这种情况，则可以使用 try...catch 把 if...then 语句包裹起来。
            try {
                if (XMLHttpRequest.DONE === xhr.readyState) {
                    if (200 === xhr.status) {
                        console.log(xhr)
                        console.log(xhr.response)
                        console.log(xhr.getAllResponseHeaders())
                        saveOrDelete()

                    } else {
                        console.error(xhr.response)
                        toastBody.textContent = xhr.response
                        new bootstrap.Toast(liveToast).show()
                    }
                }
            } catch (e) {
                console.error('Caught Exception: ' + e.description);
            }
        });
    }

    /**
     * 添加、修改或者删除操作之后的查询是一致的
     */
    function saveOrDelete() {
        let currentPage = document.getElementsByClassName('currentPage'),
            array = getDirectionAndProperty().split(', '),
            pageNum;

        // 如果当前页数大于 0，说明总页数大于 1
        if (0 < currentPage.length)
            // 获取当前页数
            pageNum = currentPage[0].getAttribute('name')
        else
            // 否则就是只有一页，将页数设为第一页
            pageNum = 0
        findEmployeesBy(pageNum, array[0], array[1])
    }

    // create div
    // 如果在事件内创建，则 blur 事件中无法删除，提示 'The node to be removed is not a child of this node.'
    const div = document.createElement('div'),
        // create ul
        ul = document.createElement('ul');
    /**
     * 对整个 document 的监听是目前最好最简单的方案，但是可能会损耗更多的性能
     * test document.activeElement
     */
    document.addEventListener('click', e => {
        // 兼容性处理
        const event = e || window.event,
            target = event.target || event.srcElement

        // 当点击的不是搜索框、也不是 autocomplete 搜索提示的内容、并且 div 已经添加到 html 中
        if (sectionDivInput !== target && !div.contains(target) && sectionDiv.contains(div))
            sectionDiv.removeChild(div);
    });

    /**
     * 使搜索提示框 width 与 input width 保持一致
     */
    window.addEventListener('resize', () => {
        div.style.width = getComputedStyle(sectionDivInput)['width'];
    });

    /**
     * 搜索框内容的每一次变动都将触发一次根据内容的搜索
     */
    const focusInput = ['focus', 'input']
    focusInput.forEach(function (type) {
        sectionDivInput.addEventListener(type, () => {
            // 写成 switch 是用于下面的测试:
            // console.count();
            // console.trace();
            switch (type) {
                case 'focus':
                    autocomplete();
                    break;
                case 'input':
                    autocomplete();
            }
        });
    })

    /**
     * 根据搜索框内容执行搜索任务
     */
    function autocomplete() {

        console.count();
        console.trace();

        let xhr;

        if (window.XMLHttpRequest) {
            xhr = new XMLHttpRequest()
        } else if (window.ActiveXObject) {
            xhr = new ActiveXObject('Microsoft.XMLHTTP')
        }

        xhr.open('GET', 'searchRecord/findRecordNamesBy?searchGroupBy=' + sectionDivSelect.value + '&recordName=' + sectionDivInput.value)

        xhr.setRequestHeader('Cache-Control', 'no-cache')

        xhr.setRequestHeader('X-CSRF-Token', token);

        xhr.responseType = 'json'

        xhr.send()

        xhr.addEventListener('readystatechange', () => {

                try {
                    if (XMLHttpRequest.DONE === xhr.readyState) {

                        if (200 === xhr.status) {

                            console.log(xhr);
                            console.log(xhr.response);
                            console.log(xhr.getAllResponseHeaders());

                            // 如果 response 没有数据并且 div 已添加到 html 则删除 div
                            if (0 === xhr.response.length && sectionDiv.contains(div))
                                sectionDiv.removeChild(div);

                            // set div
                            div.style.backgroundColor = '#303134';
                            div.style.left = '132px';
                            // 悬浮
                            div.style.position = 'absolute';
                            div.style.top = '38px';
                            // Get width of the input
                            div.style.width = getComputedStyle(sectionDivInput)['width'];

                            // set ul
                            // 重置内容
                            ul.innerHTML = '';
                            ul.style.listStyle = 'none';
                            ul.style.margin = '0';
                            ul.style.padding = '0';

                            const clickMouseoutMouseover = ['click', 'mouseout', 'mouseover']

                            // 如果此用户有历史搜索记录
                            if (0 < xhr.response.length) {

                                // 因为需要获取 xhr.response[i] 所以使用 for in
                                // create li
                                for (const i in xhr.response) {

                                    // 每次循环都需要创建一个 li
                                    const li = document.createElement('li')

                                    // set style
                                    li.style.height = '33px'
                                    li.innerHTML =
                                        '<div style="display: flex; margin: 0 20px 0 14px; align-items: Center; top: 50%;">' +

                                        '<div style="margin: 0 13px 0 1px;">' +
                                        '<i class="bi bi-clock"></i>' +
                                        '</div>' +

                                        '<div style="flex: auto; height: 33px; padding: 6px 0;">' +
                                        '<div style="height: 21px; padding: 0 8px 0 0;">' +
                                        '<span style="color: #C475F0; cursor: default; display: block; height: 21px;">' + xhr.response[i] + '</span>' +
                                        '</div>' +
                                        '</div>' +

                                        '<div>' +
                                        '<div id="deleteByRecordName" style="cursor: pointer; height: 17px;" onmouseover="this.style.color = `#3F7DF3`; this.style.textDecoration = `underline`" onmouseout="this.style.color = ``; this.style.textDecoration = ``">delete</div>' +
                                        '</div>' +

                                        '</div>';


                                    /**
                                     * 添加事件
                                     * 聚焦/失焦改变背景颜色
                                     * 点击历史搜索记录之一可以查询数据
                                     * 虽然写在这里会使耦合提高，但是方便添加事件和获取数据 ---- xhr.response[i]
                                     * 如果不写在这里，在外部是无法获取到 li 的
                                     * @type {string[]}
                                     */
                                    clickMouseoutMouseover.forEach(function (type) {
                                        li.addEventListener(type, e => {
                                            // 兼容性处理
                                            const event = e || window.event,
                                                target = event.target || event.srcElement

                                            switch (type) {
                                                case 'mouseout':
                                                    li.style.backgroundColor = '#303134';
                                                    break;

                                                case 'mouseover':
                                                    li.style.backgroundColor = '#3C4043';
                                                    break;

                                                case 'click':
                                                    if ('deleteByRecordName' !== target.id) {
                                                        // 执行搜索请求
                                                        // 将当前点击的值填充进 input
                                                        sectionDivInput.value = xhr.response[i];
                                                        sectionDivInput.blur();
                                                        reset();
                                                        // 发送搜索请求
                                                        findEmployeesBy(0, '', '')
                                                        // 保存搜索记录
                                                        saveSearchRecord(sectionDivSelect.value, sectionDivInput.value)
                                                        sectionDiv.removeChild(div);

                                                    } else {
                                                        // 点击 delete
                                                        // 发送删除搜索记录请求
                                                        let xhrDeleteByRecordName

                                                        if (window.XMLHttpRequest)
                                                            xhrDeleteByRecordName = new XMLHttpRequest();
                                                        else if (window.ActiveXObject)
                                                            xhrDeleteByRecordName = new ActiveXObject('Microsoft.XMLHTTP');

                                                        xhrDeleteByRecordName.open('DELETE', 'searchRecord/deleteByRecordName?searchGroupBy=' + sectionDivSelect.value + '&recordName=' + xhr.response[i]);

                                                        xhrDeleteByRecordName.setRequestHeader('Cache-Control', 'no-cache');

                                                        xhrDeleteByRecordName.setRequestHeader('X-CSRF-Token', token);

                                                        xhrDeleteByRecordName.send();

                                                        xhrDeleteByRecordName.addEventListener('readystatechange', () => {

                                                            try {
                                                                if (XMLHttpRequest.DONE === xhrDeleteByRecordName.readyState) {

                                                                    if (200 === xhrDeleteByRecordName.status) {

                                                                        console.log(xhrDeleteByRecordName);
                                                                        console.log(xhrDeleteByRecordName.response);
                                                                        console.log(xhrDeleteByRecordName.getAllResponseHeaders());

                                                                        autocomplete();

                                                                    } else {
                                                                        console.error(xhrDeleteByRecordName.response)
                                                                        toastBody.textContent = xhrDeleteByRecordName.response
                                                                        new bootstrap.Toast(liveToast).show()
                                                                    }
                                                                }
                                                            } catch (e) {
                                                                console.error('Caught Exception: ' + e.description);
                                                            }
                                                        })
                                                    }
                                            }
                                        })
                                    })
                                    // 每次循环的设置结束都将 li 添加到 ul
                                    ul.appendChild(li)
                                }
                            }

                            // 如果搜索记录不足 10 条，并且搜索框有值，则在整个 employee 数据表中搜索与之相关联的数据
                            if (10 > ul.getElementsByTagName('li').length && '' !== sectionDivInput.value) {
                                let xhr2;

                                if (window.XMLHttpRequest)
                                    xhr2 = new XMLHttpRequest()
                                else if (window.ActiveXObject)
                                    xhr2 = new ActiveXObject('Microsoft.XMLHTTP')

                                xhr2.open('GET', 'searchRecord/findAllPropertiesOfEmployeesBy?recordNames=' +
                                    xhr.response + '&' + sectionDivSelect.value + '=' + sectionDivInput.value)

                                xhr2.setRequestHeader('Cache-Control', 'no-cache')

                                xhr2.setRequestHeader('X-CSRF-Token', token);

                                xhr2.responseType = 'json'

                                xhr2.send()

                                xhr2.addEventListener('readystatechange', () => {

                                    try {
                                        if (XMLHttpRequest.DONE === xhr2.readyState) {

                                            if (200 === xhr2.status) {

                                                console.log(xhr2);
                                                console.log(xhr2.response);
                                                console.log(xhr2.getAllResponseHeaders());

                                                const response = xhr2.response;
                                                const length = xhr2.response.length;

                                                // 如果没有相关的搜索记录并且此 response 也没有数据并且 div 已添加到 html 则删除 div
                                                if (0 === xhr.response.length && 0 === length && sectionDiv.contains(div))
                                                    sectionDiv.removeChild(div);

                                                // 如果此用户有历史搜索记录
                                                if (0 < length)

                                                    // create li
                                                    for (const i in response) {

                                                        // 每次循环都需要创建一个 li
                                                        const li = document.createElement('li')

                                                        // set style
                                                        li.style.height = '33px'
                                                        li.innerHTML =
                                                            '<div style="display: flex; margin: 0 20px 0 14px; align-items: Center; top: 50%;">' +

                                                            '<div style="margin: 0 13px 0 1px;">' +
                                                            '<i class="bi bi-search"></i>' +
                                                            '</div>' +

                                                            '<div style="flex: auto; height: 33px; padding: 6px 0;">' +
                                                            '<div style="height: 21px; padding: 0 8px 0 0;">' +
                                                            '<span style="cursor: default; display: block; height: 21px;">' + response[i] + '</span>' +
                                                            '</div>' +
                                                            '</div>' +

                                                            '</div>';

                                                        /**
                                                         * 添加事件
                                                         * 聚焦/失焦改变背景颜色
                                                         * 点击历史搜索记录之一可以查询数据
                                                         * 写在这里方便获取数据 ---- xhr.response[i]
                                                         * @type {string[]}
                                                         */
                                                        clickMouseoutMouseover.forEach(function (type) {
                                                            li.addEventListener(type, () => {
                                                                switch (type) {
                                                                    case 'mouseout':
                                                                        li.style.backgroundColor = '#303134';
                                                                        break;

                                                                    case 'mouseover':
                                                                        li.style.backgroundColor = '#3C4043';
                                                                        break;

                                                                    case 'click':
                                                                        // 执行搜索请求
                                                                        // 将当前点击的值填充进 input
                                                                        sectionDivInput.value = response[i];
                                                                        sectionDivInput.blur();
                                                                        reset();
                                                                        // 发送搜索请求
                                                                        findEmployeesBy(0, '', '')
                                                                        // 保存搜索记录
                                                                        saveSearchRecord(sectionDivSelect.value, sectionDivInput.value)
                                                                        sectionDiv.removeChild(div);
                                                                }
                                                            });
                                                        })
                                                        ul.appendChild(li)
                                                    }
                                            }
                                        }
                                    } catch (e) {
                                        console.error('Caught Exception: ' + e.description);
                                    }
                                })
                            }
                            div.appendChild(ul);
                            sectionDiv.appendChild(div);
                        } else {
                            console.error(xhr.response)
                            toastBody.textContent = xhr.response
                            new bootstrap.Toast(liveToast).show()
                        }
                    }
                } catch (e) {
                    console.error('Caught Exception: ' + e.description);
                }
            }
        );
    }

    /**
     * 查找员工
     * 尝试过把下面两个方法写进一个方法里，但不理想
     * 键盘回车事件
     * keypress 相对于 keydown 与 keyup，只有按下 Enter 键会触发此事件。
     * 而 keydown 与 keyup，按下 Shift、Ctrl、Caps 都会触发，所以这里选择 keypress
     */
    sectionDivInput.addEventListener('keypress', e => {
        // 兼容性处理
        const event = e || window.event,
            target = event.target || event.srcElement

        if ('' !== target.value) {

            // keydown、keypress、keyup 都能正确获取到 key
            const key = event.key || event.code,
                // 但只有 keypress 能正确获取到 e.charCode 的值
                // keydown、keyup 获取为 0
                keyCode = event.keyCode || event.which || event.charCode

            if ('Enter' === key || 13 === keyCode) {
                target.blur()
                reset()
                // 发送搜索请求
                findEmployeesBy(0, '', '')
                // 保存搜索记录
                saveSearchRecord(sectionDivSelect.value, sectionDivInput.value)
            }
        }
    });

    /**
     * 搜索员工按钮点击事件
     */
    sectionDiv.getElementsByTagName('a')[0].addEventListener('click', e => {
        // 兼容性处理
        const event = e || window.event,
            target = event.target || event.srcElement

        if ('' !== target.previousElementSibling.value) {
            reset()
            // 发送搜索请求
            findEmployeesBy(0, '', '')
            // 保存搜索记录
            saveSearchRecord(sectionDivSelect.value, sectionDivInput.value)
        }
    });

    /**
     * 保存搜索记录
     * @param attribute 属性
     * @param property 值
     */
    function saveSearchRecord(attribute, property) {
        // 如果变量 xhr 在全局范围内使用，它会在 makeRequest() 函数中被相互覆盖，从而导致资源竞争。
        // 为了避免这个情况，请在包含 AJAX 函数的闭包中声明 xhr 变量。
        let xhr

        // Old compatibility code, no longer needed.
        if (window.XMLHttpRequest) // Mozilla, Safari, IE7+ ...
            xhr = new XMLHttpRequest();
        else if (window.ActiveXObject) // IE 6 and older
            xhr = new ActiveXObject('Microsoft.XMLHTTP')

        xhr.open(
            'POST',
            'searchRecord/saveSearchRecord?' + attribute + '=' + property
        )

        // 这似乎是默认设置？
        // GET 请求则设置此参数
        // 如果不设置响应头 Cache-Control: no-cache 浏览器将会把响应缓存下来而且再也无法重新提交请求。
        // 你也可以添加一个总是不同的 GET 参数，比如时间戳或者随机数
        xhr.setRequestHeader('Cache-Control', 'no-cache')

        // 因为开启了 csrf，所以增加请求头
        xhr.setRequestHeader('X-CSRF-Token', token)

        xhr.send();

        // callback
        xhr.addEventListener('readystatechange', () => {

            // 在通信错误的事件中（例如服务器宕机），在访问响应状态 onreadystatechange 方法中会抛出一个例外。
            // 为了缓和这种情况，则可以使用 try...catch 把 if...then 语句包裹起来。
            try {
                if (XMLHttpRequest.DONE === xhr.readyState) {
                    if (200 === xhr.status) {

                        console.log(xhr);
                        console.log(xhr.response);
                        console.log(xhr.getAllResponseHeaders());

                    } else {
                        console.error(xhr.response)
                        toastBody.textContent = xhr.response
                        new bootstrap.Toast(liveToast).show()
                    }
                }
            } catch (e) {
                console.error('Caught Exception: ' + e.description);
            }
        })
    }

    let employee = {
        name: 0,
        sex: 0,
        age: 0,
        idCard: 0,
        address: 0,
        phoneNumber: 0,
        createdBy: 0,
        createdDate: 0,
        lastModifiedDate: 0
    };

    /**
     * 将 employee 所有属性重置
     */
    function init() {
        employee = {
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

    /**
     * init() 将 employee 所有属性重置
     * 重新加载页面数据后需要清除之前保存在 thead tr th 中的 tbody tr 下标
     * 初始化 class
     */
    function reset() {
        init()

        for (let i = 1; i < 10; i++) {
            let th = theadTrThCollectionOf[i]

            th.valueOf().value = ''
            th.getElementsByTagName('i')[0].setAttribute('class', 'bi bi-chevron-expand')
        }
    }

    Array.from(theadTrThCollectionOf).forEach(th => {
        th.addEventListener('click', e => {
            // 兼容性处理
            const event = e || window.event,
                target = event.target || event.srcElement

            if ('TH' === target.nodeName) {
                const cellIndex = target.cellIndex;
                switch (cellIndex) {
                    case 1:
                        switch (employee.name) {
                            case 0:
                                // 后台 direction 默认就是 ASC
                                sortDirection(target, '', 'employeeName', 'bi bi-chevron-up', 'ASC')
                                employee.name = 1
                                break
                            case 1:
                                sortDirection(target, 'DESC', 'employeeName', 'bi bi-chevron-down', 'DESC')
                                employee.name = 2
                                break
                            case 2:
                                // 使第三次点击回到初始状态
                                sortDirection(target, '', '', 'bi bi-chevron-expand', '')
                        }
                        break
                    case 2:
                        switch (employee.sex) {
                            case 0:
                                sortDirection(target, '', 'employeeSex', 'bi bi-chevron-up', 'ASC')
                                employee.sex = 1
                                break
                            case 1:
                                sortDirection(target, 'DESC', 'employeeSex', 'bi bi-chevron-down', 'DESC')
                                employee.sex = 2
                                break
                            case 2:
                                sortDirection(target, '', '', 'bi bi-chevron-expand', '')
                        }
                        break
                    case 3:
                        switch (employee.age) {
                            case 0:
                                sortDirection(target, '', 'employeeAge', 'bi bi-chevron-up', 'ASC')
                                employee.age = 1
                                break
                            case 1:
                                sortDirection(target, 'DESC', 'employeeAge', 'bi bi-chevron-down', 'DESC')
                                employee.age = 2
                                break
                            case 2:
                                sortDirection(target, '', '', 'bi bi-chevron-expand', '')
                        }
                        break
                    case 4:
                        switch (employee.idCard) {
                            case 0:
                                sortDirection(target, '', 'employeeIdCard', 'bi bi-chevron-up', 'ASC')
                                employee.idCard = 1
                                break
                            case 1:
                                sortDirection(target, 'DESC', 'employeeIdCard', 'bi bi-chevron-down', 'DESC')
                                employee.idCard = 2
                                break
                            case 2:
                                sortDirection(target, '', '', 'bi bi-chevron-expand', '')
                        }
                        break
                    case 5:
                        switch (employee.address) {
                            case 0:
                                sortDirection(target, '', 'employeeAddress', 'bi bi-chevron-up', 'ASC')
                                employee.address = 1
                                break
                            case 1:
                                sortDirection(target, 'DESC', 'employeeAddress', 'bi bi-chevron-down', 'DESC')
                                employee.address = 2
                                break
                            case 2:
                                sortDirection(target, '', '', 'bi bi-chevron-expand', '')
                        }
                        break
                    case 6:
                        switch (employee.phoneNumber) {
                            case 0:
                                sortDirection(target, '', 'employeePhoneNumber', 'bi bi-chevron-up', 'ASC')
                                employee.phoneNumber = 1
                                break
                            case 1:
                                sortDirection(target, 'DESC', 'employeePhoneNumber', 'bi bi-chevron-down', 'DESC')
                                employee.phoneNumber = 2
                                break
                            case 2:
                                sortDirection(target, '', '', 'bi bi-chevron-expand', '')
                        }
                        break
                    case 7:
                        switch (employee.createdBy) {
                            case 0:
                                sortDirection(target, '', 'createdBy', 'bi bi-chevron-up', 'ASC')
                                employee.createdBy = 1
                                break
                            case 1:
                                sortDirection(target, 'DESC', 'createdBy', 'bi bi-chevron-down', 'DESC')
                                employee.createdBy = 2
                                break
                            case 2:
                                sortDirection(target, '', '', 'bi bi-chevron-expand', '')
                        }
                        break
                    case 8:
                        switch (employee.createdDate) {
                            case 0:
                                // 后台 property 默认就是 createdDate
                                sortDirection(target, '', '', 'bi bi-chevron-up', 'ASC')
                                employee.createdDate = 1
                                break
                            case 1:
                                sortDirection(target, 'DESC', '', 'bi bi-chevron-down', 'DESC')
                                employee.createdDate = 2
                                break
                            case 2:
                                sortDirection(target, '', '', 'bi bi-chevron-expand', '')
                        }
                        break
                    case 9:
                        switch (employee.lastModifiedDate) {
                            case 0:
                                sortDirection(target, '', 'lastModifiedDate', 'bi bi-chevron-up', 'ASC')
                                employee.lastModifiedDate = 1
                                break
                            case 1:
                                sortDirection(target, 'DESC', 'lastModifiedDate', 'bi bi-chevron-down', 'DESC')
                                employee.lastModifiedDate = 2
                                break
                            case 2:
                                sortDirection(target, '', '', 'bi bi-chevron-expand', '')
                        }
                }

                /**
                 * 调用发送排序方法
                 * @param target 当前 th
                 * @param direction 排序规则 ASC (Default), DESC
                 * @param property 需要排序的字段
                 * @param value class 样式
                 * @param val 将当前的排序规则 value 添加到当前的 target
                 */
                function sortDirection(target, direction, property, value, val) {
                    findEmployeesBy(0, direction, property)
                    target.getElementsByTagName('i')[0].setAttribute('class', value)
                    target.value = val
                    init()
                }

                // 选中并删除其他 thead tr th 的 val()
                // 只选取支持排序功能的 th
                const ths = []
                for (let i = 1; i < 10; i++) {
                    ths.push(theadTrThCollectionOf[i])
                }

                // 去除当前 th，不包括自身 cellIndex - 1
                ths.splice(cellIndex - 1, 1)

                // Init, delete th's 'val()' & change i's 'class'.
                for (let th of ths) {
                    th.value = ''
                    th.getElementsByTagName('i')[0].setAttribute('class', 'bi bi-chevron-expand')
                }
            }
        })
    });

    /**
     * 翻页
     * keypress 相对于 keydown 与 keyup，只有按下 Enter 键会触发此事件。
     * 而 keydown 与 keyup，按下 Shift、Ctrl、Caps 都会触发，所以这里选择 keypress
     * @type {string[]}
     */
    const clickKeypress = ['click', 'keypress'];
    clickKeypress.forEach(function (type) {
        tfoot.addEventListener(type, e => {
            // 兼容性处理
            const event = e || window.event,
                target = event.target || event.srcElement,

                /**
                 * 如果在事件外定义则无法正确获取，因为翻页后标签已被重载
                 */
                inputPageNumber = document.getElementById('pageNumber'),
                pageNumber = inputPageNumber.valueOf().value,
                strings = getDirectionAndProperty().split(', ');

            switch (type) {
                case 'click':
                    const nodeName = target.nodeName;

                    /**
                     * 首页、上一页、中间页、下一页、尾页
                     */
                    if ('A' === nodeName)
                        findEmployeesBy(target.name, strings[0], strings[1])

                    /**
                     * 通过输入框翻页的 click 事件
                     * 如果是通过输入数字翻页，点击除 input 以外的 tfoot 子标签时
                     */
                    else if (/\d/.test(pageNumber) && inputPageNumber !== target && !('A' === nodeName))
                        pageTurningRequest()
                    break

                case 'keypress':
                    /**
                     * 通过输入框翻页的 keypress event
                     */
                    if (/\d/.test(pageNumber) && inputPageNumber === target) {

                        // keydown、keypress、keyup 都能正确获取到 key
                        const key = event.key || event.code,
                            // 但只有 keypress 能正确获取到 e.charCode 的值
                            // keydown、keyup 获取为 0
                            keyCode = event.keyCode || event.which || event.charCode

                        if ('Enter' === key || 13 === keyCode)
                            pageTurningRequest()
                    }
            }

            /**
             * 发送数据，调用查询请求
             */
            function pageTurningRequest() {
                /**
                 * 如果在事件外定义则无法正确获取，因为翻页后标签已被重载
                 * @type {HTMLCollectionOf<Element>}
                 */
                const pageLink = document.getElementsByClassName('page-link');

                // 有效页数: 1.必须为整数 2.大于零 3.不大于总页数
                if (0 === pageNumber % 1 && 0 < pageNumber && parseInt(pageLink[pageLink.length - 1].name) + 2 > pageNumber) {
                    findEmployeesBy(pageNumber - 1, strings[0], strings[1])
                    // 发送请求后将 target.valueOf().value 的值设为 ''，以避免失焦后再次发送请求
                    inputPageNumber.valueOf().value = '';

                } else {
                    toastBody.textContent = '请输入正确的页数。'
                    new bootstrap.Toast(liveToast).show()
                    inputPageNumber.valueOf().value = '';
                }
            }
        })
    });

    /**
     * 发送增删改之后的页面数据更新和翻页的请求
     * @param pageNum 页数
     * @param direction 排序规则，ASC 升序，DESC 降序
     * @param property 根据该字段排序，默认 createdDate 添加时间
     */
    function findEmployeesBy(pageNum, direction, property) {
        if (pageNum > -1) {

            // 如果变量 xhr 在全局范围内使用，它会在 makeRequest() 函数中被相互覆盖，从而导致资源竞争。
            // 为了避免这个情况，请在包含 AJAX 函数的闭包中声明 xhr 变量。
            let xhr

            // Old compatibility code, no longer needed.
            if (window.XMLHttpRequest) // Mozilla, Safari, IE7+ ...
                xhr = new XMLHttpRequest();
            else if (window.ActiveXObject) // IE 6 and older
                xhr = new ActiveXObject('Microsoft.XMLHTTP')

            xhr.open(
                'GET',
                'employee/findEmployeesBy?pageNum=' + pageNum + '&pageSize=' + 10 + '&direction=' + direction +
                '&property=' + property + '&' + sectionDivSelect.value + '=' + sectionDivInput.value
            )

            // 这似乎是默认设置？
            // GET 请求则设置此参数
            // 如果不设置响应头 Cache-Control: no-cache 浏览器将会把响应缓存下来而且再也无法重新提交请求。
            // 你也可以添加一个总是不同的 GET 参数，比如时间戳或者随机数
            xhr.setRequestHeader('Cache-Control', 'no-cache')

            // 因为开启了 csrf，所以增加请求头
            xhr.setRequestHeader('X-CSRF-Token', token)

            xhr.send();

            // callback
            xhr.addEventListener('readystatechange', () => {

                // 在通信错误的事件中（例如服务器宕机），在访问响应状态 onreadystatechange 方法中会抛出一个例外。
                // 为了缓和这种情况，则可以使用 try...catch 把 if...then 语句包裹起来。
                try {
                    if (XMLHttpRequest.DONE === xhr.readyState) {
                        if (200 === xhr.status) {

                            console.log(xhr);
                            console.log(xhr.getAllResponseHeaders());

                            // 如果有数据则填充进 table
                            if (/btn-primary/.test(xhr.response))
                                updatePage(xhr.response)

                            else if (pageNum > 0)
                                // 如果该页没有数据，将会向前面一页查询数据
                                findEmployeesBy(pageNum - 1, direction, property)

                            else {
                                tbody.innerHTML = ''
                                tfootTrTdDiv.innerHTML = ''
                            }

                        } else {
                            console.error(xhr.response)
                            toastBody.textContent = xhr.response
                            new bootstrap.Toast(liveToast).show()
                        }
                    }
                } catch (e) {
                    console.error('Caught Exception: ' + e.description);
                }
            })
        } else
            findEmployeesBy(0, direction, property)
    }
})();