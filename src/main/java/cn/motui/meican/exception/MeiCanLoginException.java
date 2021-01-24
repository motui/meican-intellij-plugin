package cn.motui.meican.exception;

/**
 * @author it.motui
 * @date 2021-01-16
 */
public class MeiCanLoginException extends RuntimeException {

  public MeiCanLoginException() {
    super();
  }

  public MeiCanLoginException(String message) {
    super(message);
  }

  public MeiCanLoginException(String message, Throwable cause) {
    super(message, cause);
  }

  public MeiCanLoginException(Throwable cause) {
    super(cause);
  }

  protected MeiCanLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
