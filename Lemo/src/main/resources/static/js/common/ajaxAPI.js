/**
 *  AJAX API
 */

// AJAX API
function ajaxAPI(url, jsonData, method) {
	return new Promise(function(resolve, reject) {
		const xhr = new XMLHttpRequest();

		xhr.onload = function() {
			if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
				resolve(xhr.response);
			} else {
				reject({ status: xhr.status, statusText: xhr.statusText });
			}
		}

		xhr.open(method, contextPath + url);
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.setRequestHeader(header, token);
		xhr.responseType = "json";

		if(method == "get" || method == "GET")
		    xhr.send();
		else
		    xhr.send(JSON.stringify(jsonData)); //post body json 방식 일때
	});
};