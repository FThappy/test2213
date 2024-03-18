package app.repository.orders;

public interface OrdersRepository {
    public <T> T queryOrderUtilsForSingleData(String sql, Class<T> clazz, Object[] parameter);
    public <T> T queryOrderUtilsForObject(String sql, Class<T> clazz, Object[] parameter);
}
