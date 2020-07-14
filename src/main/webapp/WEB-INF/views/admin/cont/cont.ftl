[#escape x as x?html]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
 <title>${contTmpl.title}</title>   
<meta name="Keywords" content="">
<meta name="Description" content="">
<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
<meta content=always name=referrer>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="date=no">
<meta name="format-detection" content="address=no">
<meta name="format-detection" content="email=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta content="telephone=no" name="format-detection">
<meta http-equiv="Access-Control-Allow-Origin" content="*">
</head>

<body>
     [#noescape]
     ${contTmpl.content}
     [/#noescape]
</body>
</html>
[/#escape]
