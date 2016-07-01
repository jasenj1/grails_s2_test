package com.example

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class NothingController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Nothing.list(params), model:[nothingCount: Nothing.count()]
    }

    def show(Nothing nothing) {
        respond nothing
    }

    def create() {
        respond new Nothing(params)
    }

    @Transactional
    def save(Nothing nothing) {
        if (nothing == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (nothing.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond nothing.errors, view:'create'
            return
        }

        nothing.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'nothing.label', default: 'Nothing'), nothing.id])
                redirect nothing
            }
            '*' { respond nothing, [status: CREATED] }
        }
    }

    def edit(Nothing nothing) {
        respond nothing
    }

    @Transactional
    def update(Nothing nothing) {
        if (nothing == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (nothing.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond nothing.errors, view:'edit'
            return
        }

        nothing.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'nothing.label', default: 'Nothing'), nothing.id])
                redirect nothing
            }
            '*'{ respond nothing, [status: OK] }
        }
    }

    @Transactional
    def delete(Nothing nothing) {

        if (nothing == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        nothing.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'nothing.label', default: 'Nothing'), nothing.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'nothing.label', default: 'Nothing'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
