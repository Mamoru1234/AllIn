<#ftl encoding="utf-8">
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All IN</title>
</head>
<body>
<form action="/user/general_info" method="post">
    <input type="text" value="${generalInfo.getFirstName()}" placeholder="Your first name" name="f_name">
    <input type="text" value="${generalInfo.getSurName()}" placeholder="Your surname" name="s_name">
    <input type="text" value="${generalInfo.getCountry()}" placeholder="Your country" name="country">
    <select name="gender">
        <#list genders as gender>
            <option
                <#if generalInfo.getGender() == gender>
                    selected
                </#if>
                label=${gender}
                value=${gender}
            ></option>
        </#list>
    </select>
    <input type="submit">
</form>
</body>
</html>