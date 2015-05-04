
var BASE_URI = '/acs/comments';
var DEFAULT_THREAD_ID = 'default';
var MAX_MESSAGE_LENGTH;

var serverError;

/**
 *
 */
$(function() {

  $('#information-comment').hide();
  $('#error-comment').hide();
  $('#table-comment-list').hide();

  MAX_MESSAGE_LENGTH = Number($('#hid-max-comment-length').val());

  initializeBtnPost();

  initializeErrorDialog();

  initializeTxtThreadID();

  initializeTxtComment();

  renderCommentList();
  setInterval(renderCommentList, 3000);
});

function initializeTxtThreadID() {
  var txtThreadID = $('#txt-thread-id');
  txtThreadID.on('focus', function() {
    txtThreadID.select();
    lock = 'lock';
  });
  txtThreadID.on('blur', function() {
    var threadID = $(this).val();
    if (threadID) {
      threadID = threadID.toLowerCase();
    }

    if (/^[a-z][a-z0-9_]*$/.test(threadID)) {
      // ok
    }
    else {
      threadID = DEFAULT_THREAD_ID;
      $(this).val(threadID);
    }

    $('#table-comment-list-body').children('tr').remove();
    lastTimestamp = null;
    lock = null;
    renderCommentList();
  });
  txtThreadID.on('keydown',function(e) {
    if (e.keyCode == 13) {
      txtThreadID.blur();
    }
  });
}

var oldInputValue;
function initializeTxtComment() {
  var targetElement = $('#txt-comment');
  targetElement.val('');
  targetElement.focus();
  $("#rest-number").text(MAX_MESSAGE_LENGTH);
  $('#btn-post').prop('disabled', true);

  targetElement.on('keydown',function(e) {
    var disabled = $('#btn-post').prop('disabled');
    if (!disabled && (e.ctrlKey || e.metaKey) && e.keyCode == 13) {
      $('#btn-post').click();
    }
//    if (e.ctrlKey) {
//      return false;
//    }
  });

  targetElement.on('keyup', function(e) {
    if (oldInputValue != (value = this.value)) {
      oldInputValue = value;
      var inputLength = this.value.length;
      var restLength = MAX_MESSAGE_LENGTH - inputLength;

      var numberElement = $("#rest-number");
      numberElement.text(restLength);
      if (inputLength == 0) {
        $('#btn-post').prop('disabled', true);
      }
      else if (restLength < 0) {
        if (!numberElement.hasClass('error')) {
          targetElement.addClass('error');
          numberElement.addClass('error');
          $('#btn-post').prop('disabled', true);
        }
      }
      else {
        targetElement.removeClass('error');
        numberElement.removeClass('error');
        $('#btn-post').prop('disabled', false);
      }
    }
  });
}
function realtimeKeyup(elm) {
  var v, old = elm.value;
  return function() {
    if (old != (v = elm.value)) {
      old = v;
      str = elm.value;
      $("#rest-number").text(MAX_MESSAGE_LENGTH - str.length);
    }
  }
}


function initializeBtnPost() {

  $('#btn-post').click(function() {
    var thread_id = $('#txt-thread-id').val();
    var comment = $('#txt-comment').val();
    doPost(
        thread_id,
        comment,
        function() {
          $('#txt-comment').val('');
          $("#rest-number").text(MAX_MESSAGE_LENGTH);
          $('#btn-post').prop('disabled', true);
          renderCommentList();
        },
        function() {
          $('#btn-post').prop('disabled', true);
        });
  });

}
function doPost(thread_id, comment, funcDone, funcFail) {

  var requestUrl = BASE_URI + "/";
  if (thread_id) {
    requestUrl += thread_id;
  }
  else {
    requestUrl += DEFAULT_THREAD_ID;
  }

  $.ajax({
    type: 'POST',
    url: requestUrl,
    data: {
      comment: comment,
    },

  }).done(function(data, textStatus, jqXHR) {
    funcDone();

  }).fail(function(jqXHR, textStatus, errorThrown) {
    console.log(textStatus + ', ' + jqXHR.status + ': ' + errorThrown);
    $('#dialog-ajax-error').dialog('open');

  }).always(function(data, textStatus, errorThrown) {

  });
}


function initializeErrorDialog() {

  var ajaxErrorDialog = $('<div>', {
    id: 'dialog-ajax-error',
  });
  ajaxErrorDialog.html('サーバとの通信に失敗しました。<br/>再読み込みするか、管理者に問い合わせてください。');
  $('body').append(ajaxErrorDialog);

  ajaxErrorDialog.dialog({
    title: 'エラー',
    width: 450,
    modal: true,
    autoOpen: false,
  });
}


var lastTimestamp;
var lock;
function renderCommentList(yyyy, mm, dd) {
  if(serverError) {
    return;
  }

  var requestUrl = BASE_URI + "/";

  var thread_id = $('#txt-thread-id').val();
  if (thread_id) {
    requestUrl += thread_id;
  }
  else {
    requestUrl += DEFAULT_THREAD_ID;
  }

  if (yyyy) {
    requestUrl += '/' + yyyy;

    if (mm) {
      requestUrl += '/' + mm;

      if (dd) {
        requestUrl += '/' + dd;
      }
    }
  }
  if (lock) {
    return;
  }
  lock = 'lock';

  $.ajax({
    type: 'GET',
    url: requestUrl,
    data: {
      'last': lastTimestamp,
    },

  }).done(function(data, textStatus, jqXHR) {

    if (lastTimestamp == null && data.length == 0) {
      $('#information-comment').text('メッセージはありません。');
      $('#information-comment').show();
      $('#error-comment').hide();
      $('#table-comment-list').hide();
    }
    else {
      $('#information-comment').hide();
      $('#error-comment').hide();
      $('#table-comment-list').show();
    }

    if (lastTimestamp == null) {
      $('#table-comment-list-body').children('tr').remove();
    }

    var tbody = $('#table-comment-list-body');
    var css_new_comment = (tbody.children('tr').length != 0) ? ' new-comment' : '';

    $.each(data, function(index, row) {
      var timestamp = new Date(row.timestamp.substring(0, 19));

      var tr = $('<tr>').prependTo(tbody);
      var td_comment = $('<td>', {
        text: '',
        class: 'col-comment' + css_new_comment,
      }).appendTo(tr);
      var td_timestamp = $('<td>', {
        text: toFormattedString(timestamp),
        class: 'col-timestamp' + css_new_comment,
      }).appendTo(tr);

      // 改行を<br/>にしたいので自前でエスケープ
      var comment = row.comment.replace(/</g, '&lt;').replace(/>/, '&gt;');
      td_comment.html(comment.replace(/\n/g, '<br />'));

      lastTimestamp = row.timestamp;
    });

  }).fail(function(jqXHR, textStatus, errorThrown) {
    console.log(textStatus + ', ' + jqXHR.status + ': ' + errorThrown);
    serverError = jqXHR.status;
    $('#dialog-ajax-error').dialog('open');

  }).always(function( data, textStatus, errorThrown ) {
    lock = null;

  });

}

var DAY_OF_WEEK = [
    '日',
    '月',
    '火',
    '水',
    '木',
    '金',
    '土',
];
function toFormattedString(timestamp) {
  var ret = timestamp.getFullYear()
          + '/' + ('0' + (timestamp.getMonth() + 1)).slice(-2)
          + '/' + ('0' + timestamp.getDate()).slice(-2)
          + '(' + DAY_OF_WEEK[timestamp.getDay()] + ')'
          + ' ' + ('0' + timestamp.getHours()).slice(-2)
          + ':' + ('0' + timestamp.getMinutes()).slice(-2)
          + ':' + ('0' + timestamp.getSeconds()).slice(-2);
  return ret;
}
