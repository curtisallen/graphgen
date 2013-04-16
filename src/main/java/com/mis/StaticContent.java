// package com.mis;

// import java.io.File;
// import java.io.InputStream;
// import javax.ws.rs.GET;
// import javax.ws.rs.Path;
// import javax.ws.rs.Produces;
// import javax.ws.rs.PathParam;
// import javax.ws.rs.core.Response;
// import javax.ws.rs.core.Response.ResponseBuilder;
// import javax.ws.rs.QueryParam;
// import javax.ws.rs.core.MediaType;

// /**
//  *
//  * @author curtis
//  */
// @Path("static")
// public class StaticContent {
//     @GET
//     @Path("{path:.*}")
//     public Response getFile(@PathParam("path") String path) {
//         File f = new File(path);
//         System.out.println("Path: " + "/" + path);
//         InputStream is = StaticContent.class.getResourceAsStream( path);
//         ResponseBuilder response = Response.ok((Object) is);
//         response.type(MediaType.TEXT_PLAIN);
//         return response.build();
//     }
            
// }
