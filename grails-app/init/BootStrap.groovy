import com.example.admin.*

class BootStrap {

    def springSecurityService // Injected

    def init = { servletContext ->
        // Create Roles
        Role adminRole = new Role('ROLE_ADMIN').save()
        Role userRole = new Role('ROLE_USER').save()
        
        //Create Users
        User admin = new User('admin', 'password').save()
        User user = new User('user', 'password').save()
        
        // Assign Roles to Users
        UserRole.create(admin, adminRole)
        UserRole.create(admin, userRole)
        UserRole.create(user, userRole)
        
        UserRole.withSession {
            it.flush()
            it.clear()
        }
    }
    
    def destroy = {
    }
}
