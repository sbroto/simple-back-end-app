package com.ge.digital.controllers;

import com.ge.digital.model.Info;
import com.ge.digital.services.InfoService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
public class InfoController extends RestApiController {

    private InfoService infoService;

    @Autowired
    public InfoController(InfoService infoService) {
        this.infoService = Objects.requireNonNull(infoService);
    }


    @ApiOperation(value = "Get info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token authorization retrieved from predix uaa.", required = true, dataType = "string", paramType = "header", example = "Bearer 68e34344e5-d485-466a-b55f-0324fe345rg3445")})
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Info getVersion() {
        return infoService.getInfo();
    }
}
