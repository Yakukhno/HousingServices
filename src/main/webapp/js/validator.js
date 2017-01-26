var accountRegExp = /^[1-9]\d{3}$/;
var emailRegExp = /^[\w.%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/;
var passwordRegExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,64}$/;
var accountErrorText = "Must contain 4 digit";
var emailErrorText = "Invalid email";
var passwordErrorText = "Must have at least one digit, one lowercase and uppercase letter";

function validateUser() {
    var isValid = true;
    isValid = validateEmail() && isValid;
    isValid = validatePassword("password", "helpPass", "passwordForm") && isValid;
    return isValid;
}

function validateTenant() {
    var isValid = true;
    isValid = validateAccount() && isValid;
    isValid = validateEmail() && isValid;
    isValid = validatePassword("password", "helpPass", "passwordForm") && isValid;
    return isValid;
}

function validateDispatcher() {
    var isValid = true;
    isValid = validateEmail() && isValid;
    isValid = validatePassword("password", "helpPass", "passwordForm") && isValid;
    return isValid;
}

function validateUserUpdate() {
    var isValid = true;
    var email = document.forms["registerForm"]["email"].value;
    var password = document.forms["registerForm"]["newPassword"].value;
    if (password != '') {
        isValid = validatePassword("newPassword", "helpNewPass", "newPasswordForm") && isValid;
    }
    if (email != '') {
        isValid = validateEmail() && isValid;
    }
    isValid = validatePassword("oldPassword", "helpOldPass", "oldPasswordForm") && isValid;
    return isValid;
}

function validateAccount() {
    var account = document.forms["registerForm"]["account"].value;
    var helpAccount = document.getElementById("helpAccount");
    var accountForm = document.getElementById("accountForm");
    if (!accountRegExp.test(account)) {
        helpAccount.innerHTML = accountErrorText;
        accountForm.setAttribute('class', 'form-group has-error');
        return false;
    } else {
        helpAccount.innerHTML = '';
        accountForm.setAttribute('class', 'form-group');
        return true;
    }
}

function validateEmail() {
    var email = document.forms["registerForm"]["email"].value;
    var helpEmail = document.getElementById("helpEmail");
    var emailForm = document.getElementById("emailForm");
    if (!emailRegExp.test(email)) {
        helpEmail.innerHTML = emailErrorText;
        emailForm.setAttribute('class', 'form-group has-error');
        return false;
    }  else {
        helpEmail.innerHTML = '';
        emailForm.setAttribute('class', 'form-group');
        return true;
    }
}

function validatePassword(passwordParam, helpPassParam, passwordFormParam) {
    var password = document.forms["registerForm"][passwordParam].value;
    var helpPass = document.getElementById(helpPassParam);
    var passwordForm = document.getElementById(passwordFormParam);
    if (!passwordRegExp.test(password)) {
        helpPass.innerHTML = passwordErrorText;
        passwordForm.setAttribute('class', 'form-group has-error');
        return false;
    }  else {
        helpPass.innerHTML = '';
        passwordForm.setAttribute('class', 'form-group');
        return true;
    }
}

function validateBrigade() {
    var manager = document.forms["brigadeForm"]["manager"].value;
    var message = document.getElementById("message");
    var isValid = true;
    if (manager == '') {
        message.innerHTML = '<div class="alert alert-danger" align="center" role="alert">' +
            'You should choose manager!' +
            '</div>';
        isValid = false;
    } else {
        message.innerHTML = '';
    }
    return isValid;
}