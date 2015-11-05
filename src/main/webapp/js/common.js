function ajaxSuccess() {
	document.querySelector("div#content").innerHTML = this.responseText;
}

function AJAXSubmit(oFormElement) {
	if (!oFormElement.action) {
		return;
	}
	var oReq = new XMLHttpRequest();
	oReq.onload = ajaxSuccess;
	if (oFormElement.method.toLowerCase() === "post") {
		oReq.open("post", oFormElement.action);
		oReq.send(new FormData(oFormElement));
	} else {
		var oField, sFieldType, nFile, sSearch = "";
		for (var nItem = 0; nItem < oFormElement.elements.length; nItem++) {
			oField = oFormElement.elements[nItem];
			if (!oField.hasAttribute("name")) {
				continue;
			}
			sFieldType = oField.nodeName.toUpperCase() === "INPUT" ? oField.getAttribute("type").toUpperCase() : "TEXT";
			if (sFieldType === "FILE") {
				for (nFile = 0; nFile < oField.files.length; sSearch += "&" + escape(oField.name) + "="
						+ escape(oField.files[nFile++].name))
					;
			} else if ((sFieldType !== "RADIO" && sFieldType !== "CHECKBOX") || oField.checked) {
				sSearch += "&" + escape(oField.name) + "=" + escape(oField.value);
			}
		}
		oReq.open("get", oFormElement.action.replace(/(?:\?.*)?$/, sSearch.replace(/^&/, "?")), true);
		oReq.send(null);
	}
}

function downloadSelected(){
	var selected = document.querySelectorAll("#content input:checked");
	
}