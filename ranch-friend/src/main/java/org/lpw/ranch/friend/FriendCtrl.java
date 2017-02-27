package org.lpw.ranch.friend;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(FriendModel.NAME + ".ctrl")
@Execute(name = "/friend/", key = FriendModel.NAME, code = "16")
public class FriendCtrl {
    @Inject
    private Request request;
    @Inject
    private FriendService friendService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.BETWEEN, number = {0, 3}, parameter = "state", failureCode = 5),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object query() {
        return friendService.query(request.getAsInt("state"));
    }

    @Execute(name = "create", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "friend", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "memo", failureCode = 3),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS, parameter = "friend")
    })
    public Object create() {
        friendService.create(request.get("friend"), request.get("memo"));

        return "";
    }

    @Execute(name = "pass", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "friend", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "memo", failureCode = 3),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = FriendService.VALIDATOR_EXISTS, parameter = "friend", failureCode = 4)
    })
    public Object pass() {
        friendService.pass(request.get("friend"), request.get("memo"));

        return "";
    }

    @Execute(name = "memo", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "friend", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "memo", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "memo", failureCode = 3),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = FriendService.VALIDATOR_EXISTS, parameter = "friend", failureCode = 4)
    })
    public Object memo() {
        friendService.memo(request.get("friend"), request.get("memo"));

        return "";
    }
}
