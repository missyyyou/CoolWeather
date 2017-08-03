# Intro
The content of Cool Weather is based on the API of http://www.weather.com.cn.

Here comes the description.

# API
## Province List
- URL：`http://www.weather.com.cn/data/list3/city.xml`
- Method：`GET`
- Result：`01|北京,02|上海,03|天津,04|重庆,05|黑龙江,06|吉林,07|辽宁,08|内蒙古,09|河北,10|山西,11|陕西,12|山东,13|新疆,14|西藏,15|青海,16|甘肃,17|宁夏,18|河南,19|江苏,20|湖北,21|浙江,22|安徽,23|福建,24|江西,25|湖南,26|贵州,27|四川,28|广东,29|云南,30|广西,31|海南,32|香港,33|澳门,34|台湾`

## City List
- URL：`http://www.weather.com.cn/data/list3/city16.xml`
- Method：`GET`
- Result：`1601|兰州,1602|定西,1603|平凉,1604|庆阳,1605|武威,1606|金昌,1607|张掖,1608|酒泉,1609|天水,1610|陇南,1611|临夏,1612|甘南,1613|白银`

## County List
- URL：`http://www.weather.com.cn/data/list3/city1602.xml`
- Method：`GET`
- Result：`160201|定西,160202|通渭,160203|陇西,160204|渭源,160205|临洮,160206|漳县,160207|岷县`

## County Info
- URL：`http://www.weather.com.cn/data/list3/city160203.xml`
- Method：`GET`
- Result：`160203|101160203`

## Weather Info
- URL：`http://www.weather.com.cn/data/cityinfo/101160203.html`
- Method：`GET`
- Result：`{"weatherinfo":{"city":"陇西","cityid":"101160203","temp1":"-2℃","temp2":"16℃","weather":"多云转晴","img1":"n1.gif","img2":"d0.gif","ptime":"18:00"}}`