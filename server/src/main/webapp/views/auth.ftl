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
        Auth
    </div>
    <div>${user.getUserName()}</div>
    <div>
        ${client.getClientName()}
    </div>
    <form action="/auth" method="post">
        <input type="submit">
    </form>
</div>
<form></form>
</body>
</html>