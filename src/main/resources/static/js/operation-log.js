!function () {

    'use strict'

    /**
     * @type {HTMLElement}
     */
    const liveToast = document.getElementById('liveToast'),
        /**
         * @type {Element}
         */
        noMarginTop = document.getElementsByClassName('no-margin-top')[0],
        /**
         * @type {Element}
         */
        toastBody = document.getElementsByClassName('toast-body')[0],
        /**
         * @type {String}
         */
        token = document.getElementsByName('_csrf')[0].valueOf().value,
        /**
         * @type {HTMLTableSectionElement}
         */
        tbody = document.getElementsByTagName('tbody')[0],
        /**
         * @type {HTMLTableSectionElement}
         */
        tfoot = document.getElementsByTagName('tfoot')[0],
        /**
         * @type {HTMLDivElement}
         */
        tfootTrTdDiv = tfoot.getElementsByTagName('tr')[0].getElementsByTagName('td')[0].getElementsByTagName('div')[0]
    ;

    /**
     * 通过输入框翻页的 enter 和 blur event
     * keypress 相对于 keydown 与 keyup，只有按下 Enter 键会触发此事件。
     * 而 keydown 与 keyup，按下 Shift、Ctrl、Caps 都会触发，所以这里选择 keypress
     */
    const types = ['click', 'keypress'];
    types.forEach(function (item) {
        document.getElementsByTagName('tfoot')[0].addEventListener(item, e => {
            // 兼容性处理
            const event = e || window.event;
            const target = event.target || event.srcElement;
// debugger
            console.log(document.activeElement !== target);

            /**
             * @type {HTMLElement}
             */
            const inputPageNumber = document.getElementById('pageNumber'),
                /**
                 * @type {HTMLCollectionOf<Element>}
                 */
                aPageLink = document.getElementsByClassName('page-link'),
                /**
                 * @type {Element}
                 */
                ulPagination = document.getElementsByClassName('pagination pull-right no-margin')[0];

            /**
             * 首页、上一页、中间页、下一页、尾页
             * ulPagination.contains(target) 判断 ulPagination 是否包含此 target ---- 当点击翻页相关按钮时
             */
            if ('click' === item && ulPagination.contains(target)) {
                pageTurningAjax(target.name)
                return
            }

            /**
             * 通过输入框翻页的 click 和 keypress 事件
             * ulPagination.contains(target) 判断 ulPagination 是否包含此 target ---- 当不是点击翻页相关按钮时
             */
            if (/\d/.test(inputPageNumber.valueOf().value) && !ulPagination.contains(target)) {
                if ('keypress' === item && inputPageNumber === target) {

                    const key = event.key || event.code
                    // Yes. 只有 keypress 能正确获取到 e.charCode 的值
                    // No. keydown、keyup 获取为 0
                    // Yes. keydown、keypress、keyup 都能正确获取到 key
                    const keyCode = event.keyCode || event.which || event.charCode

                    if ('Enter' === key || 13 === keyCode) {
                        sendPage()
                    }
                } else if ('click' === item && inputPageNumber !== target) {
                    sendPage()
                }
            }

            /**
             * 发送数据，调用查询请求
             */
            function sendPage() {

                // 有效页数: 1.必须为整数 2.大于零 3.不大于总页数
                const value = inputPageNumber.valueOf().value;

                if (0 === value % 1 && 0 < value && parseInt(aPageLink[aPageLink.length - 1].name) + 2 > value) {
                    pageTurningAjax(value - 1)
                    // 发送请求后将 target.valueOf().value 的值设为 ''，以避免失焦后再次发送请求
                    inputPageNumber.valueOf().value = '';

                } else {
                    toastBody.textContent = '请输入正确的页数。';
                    new bootstrap.Toast(liveToast).show();
                }
            }
        });
    })

    /**
     * 发送翻页请求
     * @param page 页数
     */
    function pageTurningAjax(page) {

        // 如果变量 xhr 在全局范围内使用，它会在 makeRequest() 函数中被相互覆盖，从而导致资源竞争。
        // 为了避免这个情况，请在包含 AJAX 函数的闭包中声明 xhr 变量。
        let xhr;

        // Old compatibility code, no longer needed.
        if (window.XMLHttpRequest) { // Mozilla, Safari, IE7+ ...
            xhr = new XMLHttpRequest();
        } else if (window.ActiveXObject) { // IE 6 and older
            xhr = new ActiveXObject('Microsoft.XMLHTTP');
        }

        xhr.open('GET', 'operationLog/findOperationLogsBy?pageNum=' + page + '&pageSize=' + 10);

        // 这似乎是默认设置？
        // GET 请求则设置此参数
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
                        // 更新页面数据
                        console.log(xhr.response);

                        if (/\d{11}/.test(xhr.response)) {
                            // 截取之后填充进页面
                            const trs = xhr.response.split('<tbody class="table-secondary">')[1].split('</tbody>'),
                                ul = trs[1].split('<div class="modal-footer no-margin-top">')[1].split('</div>');

                            tbody.innerHTML = trs[0]
                            noMarginTop.innerHTML = ul[0]

                        } else {
                            tbody.innerHTML = ''
                            tfootTrTdDiv.innerHTML = ''
                        }
                    }
                }
            } catch (e) {
                console.error('Caught Exception: ' + e.description);
            }
        });
    }

}()