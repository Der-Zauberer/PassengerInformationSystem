class PIS {
	
	/*******************/
	/* page            */
	/*******************/
	
	page(page) {
		let currentUrl = window.location.href;
		currentUrl = this.#setParameter(currentUrl, 'page', page);
		window.location.href = currentUrl;
	}
	
	/*******************/
	/* search          */
	/*******************/
	
	search() {
		const input = document.getElementById('search');
		let currentUrl = window.location.href;
		currentUrl = this.#removeParameter(currentUrl, 'page');
		currentUrl = this.#removeParameter(currentUrl, 'pageSize');
		currentUrl = input.value ? this.#setParameter(currentUrl, 'query', input.value) : this.#removeParameter(currentUrl, 'query');
		window.location.href = currentUrl;
	}
	
	resetSearch() {
		let currentUrl = window.location.href;
		currentUrl = this.#removeParameter(currentUrl, 'query');
		currentUrl = this.#removeParameter(currentUrl, 'page');
		currentUrl = this.#removeParameter(currentUrl, 'pageSize');
		window.location.href = currentUrl;
	}
	
	/*******************/
	/* import          */
	/*******************/
	
	import(url) {
		const input = document.createElement('input');
		input.type = 'file';
		input.accept = "application/json"
		input.onchange = event => {
			if(!window.FileReader) return;
			const fileReader = new FileReader();
			fileReader.onload = function(event) {
        		fetch(url, {method: 'POST', body: event.target.result, signal: AbortSignal.timeout(120000) }).then(event => {
					location.reload();
				});
    		};
    		fileReader.readAsText(event.target.files[0]);
		};
		input.click();
	}
	
	export(url) {
		window.location.href = url; 
		//fetch(url, { signal: AbortSignal.timeout(120000) }).then((event) => console.log(event));
	}
	
	/*******************/
	/* parameter       */
	/*******************/

    #setParameter(url, param, value) {
        let currentUrl = url;
		if (currentUrl.includes('?')) {
			const parameterString = '?' + currentUrl.split('?')[1];
			if (parameterString.includes('?' + param) || parameterString.includes('&' + param)) {
				currentUrl = currentUrl.replace(new RegExp('((\\?|&)' + param + ')(=[^&]*)?', 'gm'), '$1=' + value);
			} else {
                if (currentUrl.substring(currentUrl.length - 1) != '?' && currentUrl.substring(currentUrl.length - 1) != '&') currentUrl += '&';
				currentUrl += param + '=' + value;
			}
		} else {
			currentUrl += '?' + param + '=' + value;
		}	
		return currentUrl;
        
    }

    #removeParameter(url, param) {
        let currentUrl = url;
		if (currentUrl.includes('?')) {
			const parameterString = '?' + currentUrl.split('?')[1];
			if (parameterString.includes('?' + param) || parameterString.includes('&' + param)) {
				currentUrl = currentUrl.replace(new RegExp('&?' + param + '((=[^&]*)|(?=&)|(?=$))', 'gm'), '');
                if (currentUrl.substring(currentUrl.length - 1) == '&') currentUrl = currentUrl.substring(0, currentUrl.length - 1);
                currentUrl = currentUrl.replace('?&', '?');
                if (currentUrl.substring(currentUrl.length - 1) == '?') currentUrl = currentUrl.substring(0, currentUrl.length - 1);
			}
		}
		return currentUrl;
    }

    #getParameter(param) {
        return new URLSearchParams(window.location.search).get(param);
    }
	
}

const pis = new PIS();