var domains = "url.com"
function sendRequest(url){
    var httpRequest = new XMLHttpRequest();
    httpRequest.open('POST', 'http://'+domains+'/logs/record', true); 
	httpRequest.withCredentials = true;
    httpRequest.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    httpRequest.send('url='+url);

    httpRequest.onreadystatechange = function () {
        if (httpRequest.readyState == 4 && httpRequest.status == 200) {
			var data = JSON.parse(httpRequest.responseText);
			console.log(data.msg);
        }
    };
}

function checkUrl(){
    var url = window.location.href;
	sendRequest(url);
}
window.onload = checkUrl();


