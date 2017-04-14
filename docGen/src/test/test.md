
|col1|col2|col3|
|-|-|-|
|row1-col1|row1-col2|row1-col3|
|row2-col1|row2-col2|
|row3-col1|row3-col2|row3-col3|row3-col4|

|参数|类型|必须|默认值|说明|
|-----|-----|-----|-----|-----|
|alert|string|是|无|通知内容|
|title|string|否|无|通知标题|
|openPage|string|否|无|点击打开的页面。会填充到推送信息的 param 字段上，表示由哪个 App 页面打开该通知。可不填，则由默认的首页打开 |
|extras|object|否|无|自定义 Key/value 信息，以供业务使用|