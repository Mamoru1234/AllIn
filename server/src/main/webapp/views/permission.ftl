<#ftl encoding="utf-8">
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All IN</title>
</head>
<body>
<div>
    <div>
        Get Permission
    </div>
    <div>
    For : ${client.getClientName()}
    </div>
    <form action="/user/permission" method="post">
        <input type="hidden" name="client_id" value="${client.getClientID()}">
        <input type="hidden" name="user_id" value="${user.getUserID()}">
        <input type="submit">
    </form>
</div>
</body>
</html>