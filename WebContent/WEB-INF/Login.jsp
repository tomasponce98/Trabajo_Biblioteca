<%@page import="model.Usuario"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login</title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!------ Include the above in your HEAD tag ---------->
</head>
<style>
.login-container{
    margin-top: 5%;
    margin-bottom: 5%;
}
.login-form-1{
    padding: 5%;
    box-shadow: 0 5px 8px 0 rgba(0, 0, 0, 0.2), 0 9px 26px 0 rgba(0, 0, 0, 0.19);
}
.login-form-1 h3{
    text-align: center;
    color: #333;
}
.login-form-2{
    padding: 5%;
    background: #0062cc;
    box-shadow: 0 5px 8px 0 rgba(0, 0, 0, 0.2), 0 9px 26px 0 rgba(0, 0, 0, 0.19);
}
.login-form-2 h3{
    text-align: center;
    color: #fff;
}
.login-container form{
    padding: 10%;
}
.btnSubmit
{
    width: 50%;
    border-radius: 1rem;
    padding: 1.5%;
    border: none;
    cursor: pointer;
}
.login-form-1 .btnSubmit{
    font-weight: 600;
    color: #fff;
    background-color: #0062cc;
}
.login-form-2 .btnSubmit{
    font-weight: 600;
    color: #0062cc;
    background-color: #fff;
}
.login-form-2 .ForgetPwd{
    color: #fff;
    font-weight: 600;
    text-decoration: none;
}
.login-form-1 .ForgetPwd{
    color: #0062cc;
    font-weight: 600;
    text-decoration: none;
}

</style>
<body>


<div class="container login-container">
            <div class="row">
                <div class="col-md-12 login-form-1 ">
                    <h3>Sistema de Biblioteca</h3>
                    <%if (request.getAttribute("mensaje")!=null) {%>
			      	<div class="alert alert-warning alert-dismissible fade show" role="alert">
					  <%=request.getAttribute("mensaje")%>
					  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
					    <span aria-hidden="true">&times;</span>
					  </button>
					</div>
			      	<%} %>
                    <form action="LoginServlet" method="POST" name="Login">
                        <div class="form-group">
                            <input name="nombreUsuario" id="nombreUsuario" type="text" class="form-control" placeholder="Nombre de usuario" required  />
                        </div>
                        <div class="form-group">
                            <input name="password" id="password"  type="password" class="form-control" placeholder="Contrase�a"  required/>
                        </div>
                        <div class="form-group">
                            <input name="action-type" type="submit" class="btnSubmit" value="ingresar" />
                        </div>
               
                    </form>
                    <div class="card-footer">
	                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modal2">
						  �Olvidaste tu contrase�a?
						</button>
					</div>
					  <!-- Modal -->
				<div class="modal fade" id="modal2" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel2" aria-hidden="true">
				  <div class="modal-dialog" role="document">
				    <div class="modal-content">
				    <div class="modal-header">
				        <h5 class="modal-title" id="exampleModalLabel2"></h5>
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
				          <span aria-hidden="true">&times;</span>
				        </button>
				      </div>		    
				      <div class="modal-footer">
				        <form action="LoginServlet" method="GET" name="Login">
				            <div class="form-group">
	                 			<input name="email" id="email" type="email" class="form-control" placeholder="Ingrese su email" required  />
                 			</div>
                 			<div class="form-group">
				       			<button type="submit" name="action-type" value="recuperar" class="btn btn-primary">Aceptar</button>
				         <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
				         </div>
				        </form>
				        
				      </div>
				    </div>
				  </div>
				</div>
				<!-- Modal -->
                </div>
            </div>
        </div>

</body>
</html>