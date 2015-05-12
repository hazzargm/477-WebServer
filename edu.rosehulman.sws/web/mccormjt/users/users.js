
(function init() {
  refreshUserTable();
  $('form').on('submit', createUser);  
  $('#users').on('click', '.delete', deleteUser);
  $('#users').on('blur',    'input', updateUser);
})();

function refreshUserTable() {
  $('#users').load('/mccormjt/users/all', function() {
    $('#users').prepend("<tr><th>Name</th><th>Age</th><th></th></tr>");
  });
}

function createUser(event) {
  event.preventDefault();
  var user = getUserDataFromRowEvent(event);
  console.log('CREATE USER:', user);
  $.post('/mccormjt/users/create', user).done(refreshUserTable);
  return false;
}

function updateUser(event) {
  event.preventDefault();
  var user = getUserDataFromRowEvent(event);
  console.log('UPDATE USER:', user);
  $.ajax({
    url: '/mccormjt/users/update',
    type: 'PUT',
    data: user
  })
    .always(refreshUserTable);
}

function deleteUser(event) {
  var user = getUserDataFromRowEvent(event);
  console.log('REMOVE USER:', user);
   $.ajax({
    url: '/mccormjt/users/delete/' + user.id,
    type: 'DELETE'
  })
    .done(refreshUserTable);
}

function getUserDataFromRowEvent(event) {
  var user = $(event.target).closest('.user'),
      name = $('.name', user).val(),
      age  = $('.age',  user).val();

  return { name: name, age: age, id: user[0].id };
}