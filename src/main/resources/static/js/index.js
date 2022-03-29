// 定义一个存储年龄的数组
const ageArray = ['18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33'
    , '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51'
    , '52', '53', '54', '55', '56', '57', '58', '59', '60', '61', '62', '63', '64', '65'];

// Show employees' sex and age
$(document).ready(function () {
    // Get the total number of employees
    const totalNumber = $('#tbodyId tr').length;
    for (let i = 1; i <= totalNumber; i++) {
        // every employee's sex
        let sex = $('#sexSelectId' + i).attr("class");
        // every employee's age
        let age = $('#ageSelectId' + i).attr("class");
        // 根据性别让 option 选中
        if (sex === '男') {
            $('#sexSelectId' + i + " option[value='" + sex + "']").attr('selected', 'selected');
        } else {
            $('#sexSelectId' + i + " option[value='" + sex + "']").attr('selected', 'selected');
        }
        // 根据年龄让 option 选中
        for (let l = 0; l < ageArray.length; l++) {
            if (age === ageArray[l]) {
                $('#ageSelectId' + i + " option[value='" + ageArray[l] + "']").attr('selected', 'selected');
            }
        }
    }
});

// 确定删除吗
function deleteEmployee(id, input) {
    if (confirm('确定删除?')) {
        $.ajax({
            type: 'DELETE',
            // 最前面必须要加斜杠/ '/employee/deleteById/'
            url: '/employee/deleteById/' + id,
            success: function () {
                const tr = input.parentNode.parentNode.parentNode;
                const tbody = tr.parentNode;
                tbody.removeChild(tr);
                // Get the total number of employees
                const totalNumber = $('#tbodyId tr').length;
                for (let i = 0; i < totalNumber; i++) {
                    $('#tbodyId tr:eq(' + i + ') td:eq(0)').html(i + 1);
                }
            },
            error : function(){
                alert('删除失败，请检查后重试。');
            }
        })
    }
}

// 确定更改吗
function updateEmployee(id, employee) {
    console.log(JSON.stringify(employee));
    if (confirm('确定更改?')) {
        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/employee/updateEmployee',
            data: JSON.stringify(employee),
            success: function () {
                alert('更改成功。');
            },
            error : function(){
                alert('更改失败，请检查后重试。');
            }
        })
    }
}

// function save(){
//     document.getElementById('hidden').style.display = "block";
// }
// function update(){
//     this.setAttribute('value', '提交');
// }
// function updateEmployee(employee) {
//     $.post('/employee/update', {'employee': employee}, function (data) {
//
//     })
// }
//
//
// function deleteEmployee(id) {
//     alert("delete++++++" + id);
//     $.ajax(
//         '/employee/deleteById',
//         {type: "post", data: id}
//     )
// }