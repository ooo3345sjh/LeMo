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

/*
* onreadystatechange: 요청에 대한 응답을 받는 리스너
* AJAX 요청 시 XHR 객체는 상태별로 readyState가 바뀌며 그때마다 onreadystatechange에 설정한 콜백 함수가 호출된다.
* readyState: 0 -> (xhr.opened) 1 -> (xhr.HEADERS_RECEIVED) 2 -> (xhr.LOADING) 3 -> (xhr.DONE) 4
* AJAX request : jQueryAjaxAPI('POST', '/api/users', { name: 'John', age: 30 }, 'Authorization', 'Bearer <jwt-token>');
*/
function jQueryAjaxAPI(method, url, jsonData, header, token){
   var xhr = new XMLHttpRequest();
   xhr.onreadystatechange = function() { // onreadystatechange: 요청에 대한 응답을 받는 리스너
     if (xhr.readyState === xhr.DONE) { // 요청이 완료되면
       if (xhr.status === 200 || xhr.status === 201) {
         console.log(xhr.responseText);
       } else {
         console.error('Request failed with status:', xhr.status);
       }
     }
   };
   xhr.open(method, contextPath + url); // 메소드와 주소 설정

    if(method == 'get' || method == 'GET'){
        xhr.send();
    }else {
        xhr.setRequestHeader('Content-Type', 'application/json');   // 컨텐츠타입을 json으로 변환
        if(header && token) {
            xhr.setRequestHeader(header, token);
        }
        xhr.send(JSON.stringify(jsonData)); // 데이터를 stringify해서 보냄
    }

}

function jQueryAjaxAPI2(url, jsonData, post) {
  return new Promise(function(resolve, reject) {
    $.ajax({
      type: post ? "POST" : "GET",
      url: contextPath + url,
      headers: {
        header: token,
        "Content-Type": "application/json",
      },
      data: post ? JSON.stringify(jsonData) : "",
      success: function(response) {
        resolve(response);
      },
      error: function(xhr, status, error) {
          var errorMessage = xhr.responseText ? JSON.parse(xhr.responseText).message : 'Unknown error';
          reject({ status: xhr.status, statusText: errorMessage });
      },
      dataType: "json",
    });
  });
};


