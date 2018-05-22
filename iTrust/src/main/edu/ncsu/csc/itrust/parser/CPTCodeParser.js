// URL: https://www2a.cdc.gov/vaccines/iis/iisstandards/vaccines.asp?rpt=cpt
var values = $("tbody > tr", $(".table").last())
    .map((i, tr)=>{return $("td",$(tr))})
    .map((i, a)=>{return "('"+a[0].innerText.trim()+"', '"+a[3].innerText.trim()+"')"})
    .toArray().join(",\n")

console.log("INSERT INTO cptcode VALUES\n" + values + "\nON DUPLICATE KEY UPDATE Code = Code;");