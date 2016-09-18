<#ftl encoding="utf-8">
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All IN</title>
    <script src="/utils.js"></script>
</head>
<body>
<div>
    <div>Sign In</div>
    <#if wrong_input??>
        <div>Wrong pass or email</div>
    </#if>
    <form
        action="/user/sign_in"
        method="post"
        enctype="application/x-www-form-urlencoded"
        id="sign_in_form"
    >
        <input
            type="text"
            placeholder="mail"
            name="user_mail"
            id="input_mail"
        />
        <input
            type="text"
            placeholder="password"
            name="user_password"
            id="input_password"
        />
    </form>
    <script>
        var form = document.getElementById('sign_in_form');
        form.appendChild(Utils.createElement('input', {
            type: 'submit',
            onclick: function (e) {
                var mail = document.getElementById('input_mail').value;
                var password = document.getElementById('input_password').value;
                if (!mail || !password) {
                    alert('Both fields are required');
                    e.preventDefault();
                }
            }
        }))
    </script>
</div>
</body>
</html>