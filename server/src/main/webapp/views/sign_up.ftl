<#ftl encoding="utf-8">
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All IN</title>
</head>
<body>
<#if err_mail??>
<div>User with this email already exists</div>
</#if>
<form action="/user/sign_up" method="post">
    <input type="text" name="user_mail" placeholder="User mail">
    <input type="text" name="user_pass" placeholder="User password">
    <input type="submit">
</form>
</body>
</html>