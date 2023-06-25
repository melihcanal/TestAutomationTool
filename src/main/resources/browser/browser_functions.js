//setSessionVariables
sessionStorage.setItem('events', JSON.stringify([]));
sessionStorage.setItem('mouseEvents', JSON.stringify([]));
sessionStorage.setItem('input', '');

//saveJson
(console => {
  console.save = (data, filename) => {
    if (!data) {
      console.error('Console.save: No data');
      return;
    }
    if (!filename) filename = 'step_definitions.json';
    if (typeof data === 'object') {
      data = JSON.stringify(data, undefined, 4);
    }
    const blob = new Blob([data], { type: 'text/json' });
    const element = document.createElement('a');
    const event = new MouseEvent('click', {
      view: window,
      bubbles: true,
      cancelable: false,
    });

    element.download = filename;
    element.href = window.URL.createObjectURL(blob);
    element.dataset.downloadurl = ['text/json', element.download, element.href].join(':');
    element.dispatchEvent(event);
  };
})(console);

//createXPathFromElement
const createXPathFromElement = el => {
  let nodeElem = el;
  let parts = [];
  while (nodeElem && Node.ELEMENT_NODE === nodeElem.nodeType) {
    let nbOfPreviousSiblings = 0;
    let hasNextSiblings = false;
    let sibling = nodeElem.previousSibling;
    while (sibling) {
      if (sibling.nodeType !== Node.DOCUMENT_TYPE_NODE && sibling.nodeName === nodeElem.nodeName) {
        nbOfPreviousSiblings++;
      }
      sibling = sibling.previousSibling;
    }
    sibling = nodeElem.nextSibling;
    while (sibling) {
      if (sibling.nodeName === nodeElem.nodeName) {
        hasNextSiblings = true;
        break;
      }
      sibling = sibling.nextSibling;
    }
    let prefix = nodeElem.prefix ? nodeElem.prefix + ':' : '';
    let nth = nbOfPreviousSiblings || hasNextSiblings ? '[' + (nbOfPreviousSiblings + 1) + ']' : '';
    parts.push(prefix + nodeElem.localName + nth);
    nodeElem = nodeElem.parentNode;
  }
  return parts.length ? '/' + parts.reverse().join('/') : '';
};

//getLastInputElement
const getLastInputElement = eventList => {
  return eventList[eventList.length - 1].nodeName.toLowerCase() === 'input' ||
    eventList[eventList.length - 1].nodeName.toLowerCase() === 'textarea'
    ? eventList[eventList.length - 1]
    : eventList.length > 1
    ? getLastInputElement(eventList.slice(0, eventList.length - 1))
    : null;
};

//getElementByXpath
const getElementByXpath = xpath => {
  return document.evaluate(xpath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
};

//windowListeners
const checkKeyboardInput = () => {
  let input = sessionStorage.getItem('input');
  if (input !== '') {
    let events = JSON.parse(sessionStorage.getItem('events'));
    const elem = getLastInputElement(events);
    events.push({
      actionType: 'SEND_KEYS',
      nodeName: elem.nodeName,
      xpathOrCssSelector: elem.xpathOrCssSelector,
      keyword: input,
    });
    sessionStorage.setItem('input', '');
    sessionStorage.setItem('events', JSON.stringify(events));
  }
};

onclick = event => {
  checkKeyboardInput();
  let events = JSON.parse(sessionStorage.getItem('events'));
  events.push({
    actionType: 'CLICK',
    nodeName: event.target.nodeName,
    xpathOrCssSelector: createXPathFromElement(event.target),
  });
  sessionStorage.setItem('events', JSON.stringify(events));
};

onkeydown = event => {
  let input = sessionStorage.getItem('input');
  input += event.key;
  sessionStorage.setItem('input', input);
};

onmousemove = event => {
  let mouseEvents = JSON.parse(sessionStorage.getItem('mouseEvents'));
  mouseEvents.push({
    timeStamp: event.timeStamp,
    xpathOrCssSelector: createXPathFromElement(event.target),
    nodeName: event.target.nodeName,
  });
  sessionStorage.setItem('mouseEvents', JSON.stringify(mouseEvents));
  if (mouseEvents.length > 1 && mouseEvents[mouseEvents.length - 1].timeStamp - mouseEvents[mouseEvents.length - 2].timeStamp > 500) {
    let events = JSON.parse(sessionStorage.getItem('events'));
    events.push({
      actionType: 'HOVER',
      nodeName: mouseEvents[mouseEvents.length - 2].nodeName,
      xpathOrCssSelector: mouseEvents[mouseEvents.length - 2].xpathOrCssSelector,
    });
    sessionStorage.setItem('events', JSON.stringify(events));
  }
};

onscroll = () => {
  let events = JSON.parse(sessionStorage.getItem('events'));
  if (events[events.length - 1].actionType === 'SCROLL') {
    events.pop();
  }
  events.push({
    actionType: 'SCROLL',
    nodeName: 'html',
    scrollTop: document.documentElement.scrollTop,
    scrollLeft: document.documentElement.scrollLeft,
  });
  sessionStorage.setItem('events', JSON.stringify(events));
};

//stopRecordingTestScenario
(function () {
  let events = JSON.parse(sessionStorage.getItem('events'));
  events.forEach(e => delete e.nodeName);
  console.save(JSON.stringify(events, null, 4));
  sessionStorage.setItem('events', JSON.stringify([]));
})();

//
