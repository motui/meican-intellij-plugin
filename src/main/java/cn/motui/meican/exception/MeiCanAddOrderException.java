package cn.motui.meican.exception;

/**
 * @author motui
 * @date 2021-01-16
 */
public class MeiCanAddOrderException extends RuntimeException {

  public MeiCanAddOrderException() {
    super();
  }

  public MeiCanAddOrderException(String message) {
    super(message);
  }

  public MeiCanAddOrderException(String message, Throwable cause) {
    super(message, cause);
  }

  public MeiCanAddOrderException(Throwable cause) {
    super(cause);
  }

  protected MeiCanAddOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
