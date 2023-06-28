package bo.impl;

import bo.TrolleyService;
import bo.exception.TrolleyServiceImplException;
import dao.impl.TrolleyDAOImpl;
import pojo.Trolley;
import util.ConnUtils;

import java.util.List;

public class TrolleyServiceImpl implements TrolleyService {

    TrolleyDAOImpl trolleyDAO = null;

    @Override
    public List<Trolley> getTrolleyByUserId(int userId) throws TrolleyServiceImplException {
        try {
            return trolleyDAO.getTrolleyByUserId(ConnUtils.getConn(), Trolley.class, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TrolleyServiceImplException("TrolleyServiceImpl 的 getTrolleyByUserId() 有問題。");
        }
    }
}
