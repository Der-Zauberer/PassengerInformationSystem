class SWD {

    #loaded = false;
    #submitions = [];
    
    onLoad(root) {
        for (const element of root.getElementsByTagName('*')) {
            if (!this.#menu && element.classList.contains('menu')) this.#menu = element;
            else if (!this.#navigation && element.classList.contains('navigation')) this.#navigation = element;
            else if (!this.#navigationContent && element.classList.contains('navigation-content')) this.#navigationContent = element;
            else if (element.classList.contains('dropdown')) this.initializeDropdown(element);
            else if (element.classList.contains('input')) this.initializeInputObject(element);
            else if (element.classList.contains('input-buttons')) this.initializeInputButtons(element);
            else if (element.nodeName == 'CODE') {
                if (element.classList.contains("code-html")) this.highlightHtml(element);
                else if (element.classList.contains("code-css")) this.highlightCss(element);
            }
            if (element.nodeName === 'INPUT') this.initializeInput(element);
            if (element.hasAttribute('localization')) this.#localizations.push(element);
            else {
                for (const attribute of element.attributes) {
                    if (attribute.name.startsWith('localization-')) this.#attributeLocalizations.push(element);
                }
            }
            if (element.hasAttribute('include')) this.include(element, element.getAttribute('include'));
            else if (element.hasAttribute('replace')) this.replace(element, element.getAttribute('replace'));
        }
        this.initializeNavigation();
        this.initializeTableOfContents();
        this.#loaded = true;
        for (const submition of this.#submitions) submition.call();
    }
    
    doOnPostLoad(submission) {
        this.#submitions.push(submission);
    }

    onPageResize(event) {
        if (this.#navigation && this.#navigation.classList.contains('show')) this.toggleMobileNavigationMenu();
        this.hideAllDropdowns();
    }

    onPageClick(event) {
        if (this.#navigation && this.#navigation.classList.contains('show') && event.clientX > 250) this.toggleMobileNavigationMenu();
        if (!event.target.parentNode.classList || !event.target.parentNode.classList.contains('dropdown')) this.hideAllDropdowns();
    }
    
    // *********************
    // * Include Replace   *
    // *********************
    
    include(element, src) {
        fetch(src).then(response => response.text()).then(text => element.innerHTML = text).then(text => this.onLoad(element));
    }
    
    replace(element, src) {
        fetch(src).then(response => response.text()).then(text => element.innerHTML = text).then(text => this.onLoad(element)).then(text => {
            for (const child of element.children) {
                element.after(child);
            }
            element.remove();
        });
    }
    
    expose(id) {
        const element = document.getElementById(id);
        if (!element.classList.contains('hide')) return;
        element.classList.remove('hide');
        element.innerHTML = element.innerHTML.replace('<!--', '').replace('-->', '');
        this.onLoad(element);
    }
    
    cover(id) {
        const element = document.getElementById(id);
        if (element.classList.contains('hide')) return;
        element.classList.add('hide');
        element.innerHTML = '<!--' + element.innerHTML + '-->';
        this.initializeTableOfContents();
    }
    
    toggle(id) {
        const element = document.getElementById(id);
        if (element.classList.contains('hide')) this.expose(id)
        else this.cover(id);
    }
    
    // *********************
    // * Localization      *
    // *********************
    
    #localizations = [];
    #attributeLocalizations = [];
    #defaultLanguage;
    #languages = new Map();
    
    setBrowserLanguage() {
        this.setLanguage(navigator.language);
    }
    
    setLanguage(locale) {
        const src = this.#languages.has(locale) ? this.#languages.get(locale) : this.#defaultLanguage;
        if (src == undefined) return;
        const translations = new Map();
        fetch(src).then(response => response.text()).then(text => {
            const lines = text.split(/\r?\n/gm);
            for (const line of lines) {
                const translation = line.split('=');
                translations.set(translation[0], translation[1]);
            }
            for (const localization of this.#localizations) {
                const value = translations.get(localization.getAttribute('localization'));
                if (value == undefined) continue;
                localization.innerText = value;
            }
            for (const localization of this.#attributeLocalizations) {
                for (const attribute of localization.attributes) {
                    if (attribute.name.startsWith('localization-')) {
                        const name = attribute.name.substring(13);
                        const value = translations.get(attribute.value);
                        if (value == undefined) continue;
                        localization.setAttribute(name, value);
                    }
                }
            }
        });
    }
    
    setDefaultLanguage(src) {
        this.#defaultLanguage = src;
    }
    
    addLanguage(locale, src) {
        this.#languages.set(locale, src)
    }
    
    // *********************
    // * Table of Contents *
    // *********************
    
    initializeTableOfContents() {
        const headlines = [];
        for (const headline of document.getElementsByTagName('H2')) {
            if (headline.id != '') headlines.push(headline);
        }
        for (const tableOfContent of document.getElementsByClassName('table-of-contents')) {
            tableOfContent.innerHTML = '';
            for (let headline of headlines) {
                const link = document.createElement('a');
                link.href = '#' + headline.id;
                link.innerText = headline.innerText;
                link.addEventListener('click', () => this.toggleMobileNavigationMenu());
                tableOfContent.appendChild(link);
            }
        }
    }
    
    // *********************
    // * Menu              *
    // *********************
    
    #menu;
    
    setMenuFocus(string) {
        if (!this.#loaded) {
            this.#submitions.push(() => this.setMenuFocus(string));
            return;
        }
        if (string == undefined || string == '' || !this.#menu) return;
        for (const element of this.#menu.getElementsByTagName('A')) {
            if (element.innerText == string) element.classList.add('menu-active');
            else element.classList.remove('menu-active');
        }
        if (!this.#mobileNavigationMenu) return;
        for (const element of this.#mobileNavigationMenu.getElementsByTagName('A')) {
            if (element.innerText == string) element.classList.add('menu-active');
            else element.classList.remove('menu-active');
        }
    }
    
    // *********************
    // * Navigation        *
    // *********************
    
    #navigation;
    #mobileNavigationMenu;
    #navigationContent;
    #mobileNavigationMenuItems = [];
    
    initializeNavigation() {
        if (!this.#navigation && this.#menu && this.#menu.classList.contains('mobile-menu')) {
            this.#navigation = document.createElement('div');
            this.#navigation.classList.add('navigation');
            this.#navigation.classList.add('only-mobile');
            document.body.insertBefore(this.#navigation, this.#menu ? this.#menu.nextElementSibling : document.body.firstChild);
        }
        if (this.#navigation && !this.#navigationContent) this.#navigationContent = this.#navigation.nextElementSibling;
        if (this.#menu && this.#menu.classList.contains('mobile-menu') && this.#navigation) {
            if (this.#navigation.getElementsByClassName('mobile-navigation')[0]) {
                this.#mobileNavigationMenu = this.#navigation.getElementsByClassName('mobile-navigation')[0];
            } else {
                this.#mobileNavigationMenu = document.createElement('div');
                this.#mobileNavigationMenu.classList.add('mobile-navigation');
                if (this.#navigation.children.length == 0) this.#navigation.appendChild(this.#mobileNavigationMenu);
                else this.#navigation.insertBefore(this.#mobileNavigationMenu, this.#navigation.children[0]);
            }
            this.#mobileNavigationMenu.classList.add('only-mobile');
            this.#mobileNavigationMenu.innerHTML = '';
            for (const element of this.#mobileNavigationMenuItems) {
                element.classList.remove('not-mobile');
            }
            for (const element of this.#menu.children) {
                if (element.nodeName != 'A' || element.classList.contains('not-mobile') || element.classList.contains('menu-title')) continue;
                const menuItem = element.cloneNode(true);
                menuItem.addEventListener('click', () => this.toggleMobileNavigationMenu());
                this.#mobileNavigationMenu.appendChild(menuItem);
                element.classList.add('not-mobile');
                this.#mobileNavigationMenuItems.push(element);
            }
        }
    }
    
    setNavigationFocus(string) {
        if (!this.#loaded) {
            this.#submitions.push(() => this.setNavigationFocus(string));
            return;
        }
        if (string == undefined || string == '' || !this.#navigation) return;
        for (const element of this.#navigation.getElementsByTagName('A')) {
            if (element.parentElement.classList.contains('navigation-menu')) continue;
            if (element.innerText == string) element.classList.add('navigation-active');
            else element.classList.remove('menu-active');
        }
    }
    
    toggleMobileNavigationMenu() {
        if (window.innerWidth > 768 || !this.#mobileNavigationMenu) return;
        if (!this.#navigation.classList.contains('show')) {
            this.#navigation.classList.add('show');
            if (this.#navigationContent) this.#navigationContent.classList.add('navigation-content-hide');
        } else {
            if (this.#navigationContent) this.#navigationContent.classList.remove('navigation-content-hide');
            this.#navigation.classList.remove('show');
        }
    }
    
    // *********************
    // * Input             *
    // *********************
    
    initializeInput(input) {
        input.addEventListener('input', event => input.setAttribute('dirty', ''));
        input.addEventListener('focusout', event => {
            if (input.value !== 0 && input.value !== '') input.setAttribute('dirty', '');
        });
    }
    
    initializeInputObject(input) {
        let inputField = input.getElementsByTagName('input')[0];
        if (!inputField) inputField = input.getElementsByTagName('textarea')[0];
        if (!inputField) return;
        const inputError = input.getElementsByClassName('input-error')[0]
        const inputRange = input.getElementsByClassName('input-range')[0];
        if (inputRange) {
            const attribute = inputField.getAttribute('maxlength');
            inputRange.innerText = inputField.value.length + (attribute ? '/' + attribute : '');
            inputField.addEventListener('input', event => inputRange.innerText = event.target.value.length + (attribute ? '/' + attribute : ''));
        }
        if (inputError) {
            inputError.innerText = inputField.validationMessage;
            inputField.addEventListener('input', event => inputError.innerText = inputField.validationMessage);
        }
    }
    
    initializeInputButtons(input) {
        const inputField = input.getElementsByTagName('input')[0];
        if (!inputField) return;
        const buttons = input.getElementsByTagName('a');
        inputField.addEventListener('focus', event => Array.from(buttons).forEach(button => button.classList.add('active')));
        inputField.addEventListener('focusout', event => Array.from(buttons).forEach(button => button.classList.remove('active')));
    }
    
    incrementInput(element) {
        const input = element.parentElement.getElementsByTagName('input')[0];
        if (!input || !(!input.hasAttribute('max') || input.getAttribute('max') > input.value)) return;
        input.value++;
        input.dispatchEvent(new Event('input', { bubbles: true, cancelable: true }));
    }
    
    decrementInput(element) {
        const input = element.parentElement.getElementsByTagName('input')[0];
        if (!input || !(!input.hasAttribute('min') || input.getAttribute('min') < input.value)) return;
        input.value--;
        input.dispatchEvent(new Event('input', { bubbles: true, cancelable: true }));
    }
    
    // *********************
    // * Dropdown          *
    // *********************
    
    #dropdowns = [];
    
    initializeDropdown(dropdown) {
        const content = dropdown.getElementsByClassName('dropdown-content')[0];
        if (!content) return;
        if (dropdown.classList.contains('dropdown-hover')) dropdown.addEventListener('mouseover', event => this.translateDropdown(dropdown, content));
        dropdown.addEventListener('click', event => this.toggleDropdown(dropdown, content));
        this.#dropdowns.push(dropdown);
        const input = dropdown.getElementsByTagName('input')[0];
        if (input) this.initializeInputDropdown(dropdown, content, input);
    }
    
    initializeInputDropdown(dropdown, content, input) {
        const valueElements = content.getElementsByTagName('a');
        input.setAttribute("autocomplete", "off");
        let activeElement = -1;
        let visibleElemnts = [];
        input.addEventListener('click', event => {
            visibleElemnts = valueElements;
            activeElement = -1;
            for (var i = 0; i < valueElements.length; i++) valueElements[i].classList.remove('hide');
        });
        for (var i = 0; i < valueElements.length; i++) {
            if (!valueElements[i].hasAttribute('value')) valueElements[i].setAttribute('value', valueElements[i].innerHTML);
            valueElements[i].addEventListener('click', event => {
                input.value = event.target.getAttribute('value');
                event.target.classList.remove('dropdown-active');
                activeElement = -1;
            });
        }
        input.addEventListener('input', event => {
            content.classList.add('show');
            if (activeElement != -1 && visibleElemnts.length > 0) visibleElemnts[activeElement].classList.remove('dropdown-active');
            visibleElemnts = [];
            activeElement = -1;
             if (input.value == '') {
                visibleElemnts = valueElements;
                for (var i = 0; i < valueElements.length; i++) valueElements[i].classList.remove('hide');
            } else {
                for (var i = 0; i < valueElements.length; i++) {
                    if (input.value.includes(valueElements[i].getAttribute('value')) || valueElements[i].getAttribute('value').includes(input.value)) {
                        valueElements[i].classList.remove('hide');
                        visibleElemnts.push(valueElements[i]);
                    } else {
                        valueElements[i].classList.add('hide');
                    }
                }
            }
            if (visibleElemnts.length == 0) content.classList.add('hide');
            else content.classList.remove('hide');
        });
        input.addEventListener('keydown', event => {
            if (!content.classList.contains('show')) {
                if (event.keyCode == 13) content.classList.add('show');
                return;
            }
            if ((event.keyCode == 40 || event.keyCode == 38) && visibleElemnts.length > 0) {
                event.preventDefault();
                if (activeElement == -1) {
                    activeElement = event.keyCode == 40 ? 0 : visibleElemnts.length - 1;
                    visibleElemnts[activeElement].classList.add('dropdown-active');
                } else {
                    visibleElemnts[activeElement].classList.remove('dropdown-active');
                    if (event.keyCode == 40) {
                        if (activeElement + 1 < visibleElemnts.length) activeElement++;
                        else activeElement = 0;
                    } else {
                        if (activeElement - 1 >= 0) activeElement--;
                        else activeElement = visibleElemnts.length - 1;
                    }
                    visibleElemnts[activeElement].classList.add('dropdown-active');
                }
                const contentRect = content.getBoundingClientRect();
                const elementRect = visibleElemnts[activeElement].getBoundingClientRect();
                const elementOffset = visibleElemnts[activeElement].offsetTop;
                if (elementOffset + elementRect.height > content.scrollTop + contentRect.height) content.scrollTop = elementOffset + elementRect.height - contentRect.height;
                else if (elementOffset < content.scrollTop) content.scrollTop = elementOffset;
            } else if (event.keyCode == 13 && activeElement != -1) {
                valueElements[activeElement].click();
            }
        });
    }
    
    toggleDropdown(dropdown, content) {
        if (!content.classList.contains('show')) {
            this.hideAllDropdowns();
            content.classList.add('show');
            this.translateDropdown(dropdown, content);
        } else {
            content.classList.remove('show');
        }
    }
    
    hideAllDropdowns() {
        for (const element of this.#dropdowns) {
            element.getElementsByClassName('dropdown-content')[0].classList.remove('show');
        }
    }
    
    translateDropdown(dropdown, content) {
        const contentRect = content.getBoundingClientRect();
        const dropdownRect = dropdown.getBoundingClientRect();
        if (contentRect.right > window.innerWidth) content.style.cssText = 'left: -' + (contentRect.width - dropdownRect.width) + 'px;';
        if (dropdown.getElementsByTagName('input')[0]) {
            if (contentRect.bottom > window.innerHeight && window.innerHeight - contentRect.top > 100) content.style.maxHeight = (window.innerHeight - contentRect.top) + 'px';
            else content.style.maxHeight = '';
        }    
    }
    
    // *********************
    // * Dialog            *
    // *********************
    
    toggleDialog(dialogId) {
        const dialog = document.getElementById(dialogId);
        if (!dialog.classList.contains('show')) {
            this.hideAllDropdowns();
            dialog.classList.add('show');
        } else {
            dialog.classList.remove('show');
        }
    }
    
    // *********************
    // * Code Highlighting *
    // *********************
    
    highlightHtml(element) {
        const string = element.innerHTML;
        let codeString = '';
        let tag = false;
        let attribute = false;
        let value = false;
        let comment = false;
        let lastAddedIndex = 0;
        for (let i = 0; i < string.length; i++) {
            if (!tag && !comment && i + 3 < string.length && string.substring(i, i + 4) == '&lt;' && !(i + 7 < string.length && string.substring(i, i + 7) == '&lt;!--')) {
                codeString += string.substring(lastAddedIndex, i) + '<span class="blue-text">&lt;';
                tag = true;
                i += 4;
                lastAddedIndex = i;
            } else if (tag && i + 3 < string.length && string.substring(i, i + 4) == '&gt;') {
                codeString += string.substring(lastAddedIndex, i);
                if (attribute) codeString += '</span>';
                codeString += '&gt;</span>';
                tag = false;
                attribute = false;
                i += 3;
                lastAddedIndex = i + 1;
            } else if (tag && !comment && !attribute && string.charAt(i) == ' ') {
                codeString += string.substring(lastAddedIndex, i) + '<span class="green-text">';
                attribute = true;
                lastAddedIndex = i;
            } else if (tag && !comment && attribute && string.charAt(i) == '"') {
                codeString += value ? string.substring(lastAddedIndex, i) + '"</span>' : string.substring(lastAddedIndex, i) + '<span class="red-text">"';
                value = !value;
                lastAddedIndex = i + 1;
            } else if (!tag && !comment && i + 7 < string.length && string.substring(i, i + 7) == '&lt;!--') {
                codeString += string.substring(lastAddedIndex, i) + '<span class="grey-text">&lt;!--';
                comment = true;
                i += 7;
                lastAddedIndex = i;
            } else if (comment && i + 6 < string.length && string.substring(i, i + 6) == '--&gt;') {
                codeString += string.substring(lastAddedIndex, i) + '--&gt;</span>';
                comment = false;
                i += 6;
                lastAddedIndex = i;
            }
        }
        codeString += string.substring(lastAddedIndex, string.length - 1);
        element.innerHTML = codeString;
    }
    
    highlightCss(element) {
        const string = element.innerHTML;
        let codeString = '';
        let key = false;
        let value = false;
        let comment = false;
        let lastAddedIndex = 0;
        for (let i = 0; i < string.length; i++) {
            if (!comment && !key && string.charAt(i) == '{') {
                codeString += string.substring(lastAddedIndex, i) + '</span>{<span class="green-text">';
                key = true;
                lastAddedIndex = ++i;
            } else if (!comment && key && string.charAt(i) == ':') {
                codeString += string.substring(lastAddedIndex, i) + '</span>:<span class="red-text">';
                value = true;
                lastAddedIndex = ++i;
            } else if (!comment && key && string.charAt(i) == '}') {
                codeString += string.substring(lastAddedIndex, i) + '</span>}<span class="blue-text">';
                key = false;
                value = false;
                lastAddedIndex = ++i;
            } else if (!comment && value && string.charAt(i) == ';') {
                codeString += string.substring(lastAddedIndex, i) + '</span>;<span class="green-text">';
                key = true;
                value = false;
                lastAddedIndex = ++i;
            } else if (!comment && i + 1 < string.length && string.substring(i, i + 2) == '/*') {
                codeString += string.substring(lastAddedIndex, i) + '<span class="grey-text">/*';
                comment = true;
                i += 2;
                lastAddedIndex = i;
            } else if (comment && i + 1 < string.length && string.substring(i, i + 2) == '*/') {
                codeString += string.substring(lastAddedIndex, i) + '/*</span>';
                comment = false;
                i += 2;
                lastAddedIndex = i;
            }
        }
        codeString += string.substring(lastAddedIndex, string.length - 1);
        element.innerHTML = '<span class="blue-text">' + codeString + '</span>';
    }

}

const swd = new SWD();

document.addEventListener('readystatechange', event => { 
    if (event.target.readyState === 'interactive') {
        document.addEventListener('click', (event) => {
            swd.onPageClick(event);
        });
        window.addEventListener('resize', (event) => {
            swd.onPageResize(event);
        });
        swd.onLoad(document);
    }
});