package models.extensions

import java.time.OffsetDateTime

import sangria.ast.StringValue
import sangria.schema.ScalarType
import sangria.validation.ValueCoercionViolation

import scala.util.Try

case object DateTimeCoercionViolation extends ValueCoercionViolation("DateTime value expected")

object DateTimeType
    extends ScalarType[OffsetDateTime](
      "DateTime",
      coerceOutput = (odt, _) => odt.toString,
      coerceInput = {
        case StringValue(odt, _, _, _, _) => Right(OffsetDateTime.parse(odt))
        case _                            => Left(DateTimeCoercionViolation)
      },
      coerceUserInput = {
        case s: String =>
          Try(OffsetDateTime.parse(s)).toEither.left.map(_ => DateTimeCoercionViolation)
        case _ => Left(DateTimeCoercionViolation)
      }
    )
