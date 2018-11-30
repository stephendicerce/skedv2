package organizer

class BootStrap {

    def init = { servletContext ->

        new User(firstName: "Stephen", lastName: "DiCerce", email: "sdicerce@oswego.edu").save()
    }
    def destroy = {
    }
}
