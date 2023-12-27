<!-- the "login-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="login-page">
  <login-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header text-center">Welcome to Health Tracker Application</h5>
          <div v-if="!loggedIn" class="login-card">
            <div class="container">
              <div class="row">
                <div class="col-lg-4 col-md-6 col-sm-8 mx-auto">
                    <h1 align="center">Sign In</h1>
                    <form class="form-group">
                      <input v-model="emailLogin" type="email" class="form-control" placeholder="Email" required><p></p>
                      <input v-model="passwordLogin" type="password" class="form-control" placeholder="Password" required><p></p>
                      <input type="submit" class="btn btn-primary" @click="doLogin()"><p></p>
                      <p>Don't have an account? <a href="#" @click="registerActive = !registerActive, emptyFields = false">Sign up here</a>
                      </p>
                      <p><a href="#">Forgot your password?</a></p>
                    </form>
                </div>
              </div>
            </div>
          </div>
          <div v-else>
            <p>Welcome, {{ username }}!</p>
            <button @click="logout">Logout</button>
          </div>
        </div>
      </div>
    </div>
  </login-layout>
</template>

<script>
app.component('login-page',
    {
      template: "#login-page",
      data: () => ({
          username: '',
          password: '',
          loggedIn: false,
      }),
      methods: {
        doLogin: function () {
          console.log("clicked login")
          this.loggedIn = true
        }
      }
    });
</script>