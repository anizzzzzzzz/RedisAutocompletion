var typingTimerFirst;                //timer identifier
var typingTimerSecond;                //timer identifier
var doneTypingInterval = 1500;  //time in ms (1.5 seconds)

$('#text-field-first').keyup(function () {
    clearTimeout(typingTimerFirst);
    typingTimerFirst = setTimeout(autocomplete_first, doneTypingInterval);
});

$('#text-field-second').keyup(function () {
    clearTimeout(typingTimerSecond);
    typingTimerSecond = setTimeout(autocomplete_second, doneTypingInterval);
});

function autocomplete_first() {
    autoComplete('first');
}

function autocomplete_second() {
    autoComplete('second');
}

function autoComplete(version) {
    var text = document.getElementById("text-field-"+version).value;
    var search = {skill:text};
    document.getElementById("suggestion-table").innerHTML = '';
    if(version === 'first')
        url_ = '/autocomplete';
    else
        url_ = '/autocomplete';

    $.ajax({
        type:'POST',
        url : url_,
        data : JSON.stringify(search),
        contentType : 'application/json; charset=utf-8',
        dataType : 'json',
        success : function (data) {
            var tr = document.createElement("TR");
            for(var i=0; i<data.length; i++){
                var node = document.createElement("TD");
                var textNode = document.createTextNode((i+1) + ") " + data[i]);
                node.appendChild(textNode);
                if(i % 3 === 0){
                    document.getElementById("suggestion-table").appendChild(tr);
                    tr = document.createElement("TR");
                    tr.appendChild(node);
                }
                else
                    tr.appendChild(node);
            }
            document.getElementById("suggestion-table").appendChild(tr);
        }
    })
}

function onLoadBody() {
    $.ajax({
        type:'POST',
        url : '/search?query=Java',
        success : function (data) {
            console.log(data[0]);
        }
    });
}