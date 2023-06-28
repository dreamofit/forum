package cn.ihoway.user.io;

import cn.ihoway.common.io.CommonOutput;
import cn.ihoway.entity.Essay;

import java.util.ArrayList;
import java.util.List;

public class EssaySearchByUserOutput extends CommonOutput {
    public List<Essay> essayList = new ArrayList<>();
}
