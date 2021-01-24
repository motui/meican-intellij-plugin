package cn.motui.meican.exception;

/**
 * @author it.motui
 * @date 2021-01-16
 */
public class MeiCanException extends RuntimeException {

  public MeiCanException() {
    super();
  }

  public MeiCanException(String message) {
    super(message);
  }

  public MeiCanException(String message, Throwable cause) {
    super(message, cause);
  }

  public MeiCanException(Throwable cause) {
    super(cause);
  }

  protected MeiCanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
