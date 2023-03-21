package com.testautomationtool.util;

public class JSFunctions {

    public static final String setSessionVariables =
        "sessionStorage.setItem('events', JSON.stringify([]));\n" + "sessionStorage.setItem('input', '');\n";

    public static final String getSessionVariables =
        "let events = JSON.parse(sessionStorage.getItem('events'))\n" + "let input = sessionStorage.getItem('input');\n";

    public static final String saveJson =
        "((console) => {\n" +
        "    console.save = (data, filename) => {\n" +
        "        if (!data) {\n" +
        "            console.error('Console.save: No data')\n" +
        "            return;\n" +
        "        }\n" +
        "        if (!filename) filename = 'step_definitions.json'\n" +
        "        if (typeof data === \"object\") {\n" +
        "            data = JSON.stringify(data, undefined, 4)\n" +
        "        }\n" +
        "        var blob = new Blob([data], { type: 'text/json' });\n" +
        "        var a = document.createElement('a')\n" +
        "        var event = new MouseEvent('click', {\n" +
        "            view: window,\n" +
        "            bubbles: true,\n" +
        "            cancelable: false\n" +
        "        });\n" +
        "\n" +
        "        a.download = filename\n" +
        "        a.href = window.URL.createObjectURL(blob)\n" +
        "        a.dataset.downloadurl = ['text/json', a.download, a.href].join(':')\n" +
        "        a.dispatchEvent(event)\n" +
        "    }\n" +
        "})(console)\n";

    public static final String createXPathFromElement =
        "const createXPathFromElement = (element) => {\n" +
        "    var allNodes = document.getElementsByTagName('*');\n" +
        "    for (var segs = []; element && element.nodeType == 1; element = element.parentNode) {\n" +
        "        if (element.hasAttribute('id')) {\n" +
        "            var uniqueIdCount = 0;\n" +
        "            for (var n = 0; n < allNodes.length; n++) {\n" +
        "                if (allNodes[n].hasAttribute('id') && allNodes[n].id == element.id) uniqueIdCount++;\n" +
        "                if (uniqueIdCount > 1) break;\n" +
        "            };\n" +
        "            if (uniqueIdCount == 1) {\n" +
        "                segs.unshift('id(\"' + element.getAttribute('id') + '\")');\n" +
        "                return segs.join('/');\n" +
        "            } else {\n" +
        "                segs.unshift(element.localName.toLowerCase() + '[@id=\"' + element.getAttribute('id') + '\"]');\n" +
        "            }\n" +
        "        } else if (element.hasAttribute('class')) {\n" +
        "            segs.unshift(element.localName.toLowerCase() + '[@class=\"' + element.getAttribute('class') + '\"]');\n" +
        "        } else {\n" +
        "            for (i = 1, sib = element.previousSibling; sib; sib = sib.previousSibling) {\n" +
        "                if (sib.localName == element.localName) i++;\n" +
        "            };\n" +
        "            segs.unshift(element.localName.toLowerCase() + '[' + i + ']');\n" +
        "        };\n" +
        "    };\n" +
        "    return segs.length ? '/' + segs.join('/') : null;\n" +
        "};\n";

    public static final String getLastInputElement =
        "const getLastInputElement = (eventList) => {\n" +
        "    return eventList[eventList.length - 1].nodeName.toLowerCase() == 'input' ? eventList[eventList.length - 1].element : eventList.length > 0 ? getLastInputElement(eventList.slice(0, eventList.length - 1)) : null;\n" +
        "}\n";

    public static final String windowListeners =
        "window.addEventListener('click', (e) => {\n" +
        "    if (input !== '') {\n" +
        "        var elem = getLastInputElement(events);\n" +
        "        events.push({\n" +
        "            actionType: 'SEND_KEYS',\n" +
        "            element: elem,\n" +
        "            xpathOrCssSelector: createXPathFromElement(elem),\n" +
        "            keyword: elem.value\n" +
        "        });\n" +
        "        input = '';\n" +
        "    }\n" +
        "    events.push({\n" +
        "        actionType: 'CLICK',\n" +
        "        element: e.target,\n" +
        "        xpathOrCssSelector: createXPathFromElement(e.target)\n" +
        "    });\n" +
        "    sessionStorage.setItem('events', JSON.stringify(events));\n" +
        "});\n" +
        "\n" +
        "window.addEventListener('keydown', (e) => {\n" +
        "    if (e.key == 'Escape') {\n" +
        "        console.save(JSON.stringify(events, null, 4));\n" +
        "    }\n" +
        "    input += e.key;\n" +
        "    sessionStorage.setItem('input', input);\n" +
        "});\n" +
        "\n";
}
