/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;


/**
 * QD3DManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class QD3DManager {

    private QD3DManager() {
    }

    /** used by QTSession */
    public static int Q3Exit() {
        throw new UnsupportedToolboxException("Q3Exit");
    }

    /** used by QTSession */
    public static int Q3Initialize() {
        throw new UnsupportedToolboxException("Q3Initialize");
    }

    /** used by Matrix3x3 */
    public static int Q3Matrix3x3_Adjoint(byte[] matrix3x3, byte[] result) {
        throw new UnsupportedToolboxException("Q3Matrix3x3_Adjoint");
    }

    /** used by Matrix3x3 */
    public static int Q3Matrix3x3_Copy(byte[] matrix3x3, byte[] result) {
        throw new UnsupportedToolboxException("Q3Matrix3x3_Copy");
    }

    /** used by Matrix3x3 */
    public static float Q3Matrix3x3_Determinant(byte[] matrix3x3) {
        throw new UnsupportedToolboxException("Q3Matrix3x3_Determinant");
    }

    /** used by Matrix3x3 */
    public static int Q3Matrix3x3_Invert(byte[] matrix3x3, byte[] result) {
        throw new UnsupportedToolboxException("Q3Matrix3x3_Invert");
    }

    /** used by Matrix3x3 */
    public static int Q3Matrix3x3_Multiply(byte[] matrixA, byte[] matrixB, byte[] result) {
        throw new UnsupportedToolboxException("Q3Matrix3x3_Multiply");
    }

    /** used by Matrix3x3 */
    public static int Q3Matrix3x3_SetIdentity(byte[] matrix3x3) {
        throw new UnsupportedToolboxException("Q3Matrix3x3_SetIdentity");
    }

    /** used by Matrix3x3 */
    public static int Q3Matrix3x3_SetRotateAboutPoint(byte[] matrix3x3, byte[] origin, float angle) {
        throw new UnsupportedToolboxException("Q3Matrix3x3_SetRotateAboutPoint");
    }

    /** used by Matrix3x3 */
    public static int Q3Matrix3x3_SetScale(byte[] matrix3x3, float xScale, float yScale) {
        throw new UnsupportedToolboxException("Q3Matrix3x3_SetScale");
    }

    /** used by Matrix3x3 */
    public static int Q3Matrix3x3_SetTranslate(byte[] matrix3x3, float xTrans, float yTrans) {
        throw new UnsupportedToolboxException("Q3Matrix3x3_SetTranslate");
    }

    /** used by Matrix3x3 */
    public static int Q3Matrix3x3_Transpose(byte[] matrix3x3, byte[] result) {
        throw new UnsupportedToolboxException("Q3Matrix3x3_Transpose");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_Copy(byte[] matrix4x4, byte[] result) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_Copy");
    }

    /** used by Matrix4x4 */
    public static float Q3Matrix4x4_Determinant(byte[] matrix4x4) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_Determinant");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_Invert(byte[] matrix4x4, byte[] result) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_Invert");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_Multiply(byte[] matrixA, byte[] matrixB, byte[] result) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_Multiply");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_SetIdentity(byte[] matrix4x4) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_SetIdentity");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_SetQuaternion(byte[] matrix, byte[] quaternion) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_SetQuaternion");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_SetRotateAboutAxis(byte[] matrix4x4, byte[] origin, byte[] orientation, float angle) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_SetRotateAboutAxis");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_SetRotateAboutPoint(byte[] matrix4x4, byte[] origin, float xAngle, float yAngle, float zAngle) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_SetRotateAboutPoint");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_SetRotateVectorToVector(byte[] matrix4x4, byte[] v1, byte[] v2) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_SetRotateVectorToVector");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_SetRotate_X(byte[] matrix4x4, float angle) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_SetRotate_X");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_SetRotate_XYZ(byte[] matrix4x4, float xAngle, float yAngle, float zAngle) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_SetRotate_XYZ");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_SetRotate_Y(byte[] matrix4x4, float angle) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_SetRotate_Y");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_SetRotate_Z(byte[] matrix4x4, float angle) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_SetRotate_Z");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_SetScale(byte[] matrix4x4, float xScale, float yScale, float zScale) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_SetScale");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_SetTranslate(byte[] matrix4x4, float xTrans, float yTrans, float zTrans) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_SetTranslate");
    }

    /** used by Matrix4x4 */
    public static int Q3Matrix4x4_Transpose(byte[] matrix4x4, byte[] result) {
        throw new UnsupportedToolboxException("Q3Matrix4x4_Transpose");
    }

    /** used by Point2D */
    public static float Q3Point2D_Distance(byte[] p1, byte[] p2) {
        throw new UnsupportedToolboxException("Q3Point2D_Distance");
    }

    /** used by Point2D */
    public static float Q3Point2D_DistanceSquared(byte[] p1, byte[] p2) {
        throw new UnsupportedToolboxException("Q3Point2D_DistanceSquared");
    }

    /** used by Point2D */
    public static int Q3Point2D_RRatio(byte[] p1, byte[] p2, float r1, float r2, byte[] result) {
        throw new UnsupportedToolboxException("Q3Point2D_RRatio");
    }

    /** used by Point2D */
    public static int Q3Point2D_Subtract(byte[] p1, byte[] p2, byte[] result) {
        throw new UnsupportedToolboxException("Q3Point2D_Subtract");
    }

    /** used by Point2D */
    public static int Q3Point2D_Vector2D_Add(byte[] point2D, byte[] vector2D, byte[] result) {
        throw new UnsupportedToolboxException("Q3Point2D_Vector2D_Add");
    }

    /** used by Point2D */
    public static int Q3Point2D_Vector2D_Subtract(byte[] point2D, byte[] vector2D, byte[] result) {
        throw new UnsupportedToolboxException("Q3Point2D_Vector2D_Subtract");
    }

    /** used by Point3D */
    public static int Q3Point3D_CrossProductTri(byte[] point1, byte[] point2, byte[] point3, byte[] crossVector) {
        throw new UnsupportedToolboxException("Q3Point3D_CrossProductTri");
    }

    /** used by Point3D */
    public static float Q3Point3D_Distance(byte[] p1, byte[] p2) {
        throw new UnsupportedToolboxException("Q3Point3D_Distance");
    }

    /** used by Point3D */
    public static float Q3Point3D_DistanceSquared(byte[] p1, byte[] p2) {
        throw new UnsupportedToolboxException("Q3Point3D_DistanceSquared");
    }

    /** used by Point3D */
    public static int Q3Point3D_RRatio(byte[] p1, byte[] p2, float r1, float r2, byte[] result) {
        throw new UnsupportedToolboxException("Q3Point3D_RRatio");
    }

    /** used by Point3D */
    public static int Q3Point3D_Subtract(byte[] p1, byte[] p2, byte[] result) {
        throw new UnsupportedToolboxException("Q3Point3D_Subtract");
    }

    /** used by Point3D */
    public static int Q3Point3D_TransformQuaternion(byte[] point3D, byte[] quaternion, byte[] result) {
        throw new UnsupportedToolboxException("Q3Point3D_TransformQuaternion");
    }

    /** used by Point3D */
    public static int Q3Point3D_Vector3D_Add(byte[] point3D, byte[] vector3D, byte[] result) {
        throw new UnsupportedToolboxException("Q3Point3D_Vector3D_Add");
    }

    /** used by Point3D */
    public static int Q3Point3D_Vector3D_Subtract(byte[] point3D, byte[] vector3D, byte[] result) {
        throw new UnsupportedToolboxException("Q3Point3D_Vector3D_Subtract");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_Copy(byte[] quaternion, byte[] result) {
        throw new UnsupportedToolboxException("Q3Quaternion_Copy");
    }

    /** used by Quaternion */
    public static float Q3Quaternion_Dot(byte[] q1, byte[] q2) {
        throw new UnsupportedToolboxException("Q3Quaternion_Dot");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_InterpolateFast(byte[] q1, byte[] q2, float t, byte[] result) {
        throw new UnsupportedToolboxException("Q3Quaternion_InterpolateFast");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_InterpolateLinear(byte[] q1, byte[] q2, float t, byte[] result) {
        throw new UnsupportedToolboxException("Q3Quaternion_InterpolateLinear");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_Invert(byte[] quaternion, byte[] result) {
        throw new UnsupportedToolboxException("Q3Quaternion_Invert");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_IsIdentity(byte[] quaternion) {
        throw new UnsupportedToolboxException("Q3Quaternion_IsIdentity");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_MatchReflection(byte[] q1, byte[] q2, byte[] result) {
        throw new UnsupportedToolboxException("Q3Quaternion_MatchReflection");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_Multiply(byte[] q1, byte[] q2, byte[] result) {
        throw new UnsupportedToolboxException("Q3Quaternion_Multiply");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_Normalize(byte[] quaternion, byte[] result) {
        throw new UnsupportedToolboxException("Q3Quaternion_Normalize");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_SetIdentity(byte[] quaternion) {
        throw new UnsupportedToolboxException("Q3Quaternion_SetIdentity");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_SetRotateAboutAxis(byte[] quaternion, byte[] axis, float angle) {
        throw new UnsupportedToolboxException("Q3Quaternion_SetRotateAboutAxis");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_SetRotateVectorToVector(byte[] quaternion, byte[] v1, byte[] v2) {
        throw new UnsupportedToolboxException("Q3Quaternion_SetRotateVectorToVector");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_SetRotate_X(byte[] quaternion, float angle) {
        throw new UnsupportedToolboxException("Q3Quaternion_SetRotate_X");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_SetRotate_XYZ(byte[] quaternion, float xAngle, float yAngle, float zAngle) {
        throw new UnsupportedToolboxException("Q3Quaternion_SetRotate_XYZ");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_SetRotate_Y(byte[] quaternion, float angle) {
        throw new UnsupportedToolboxException("Q3Quaternion_SetRotate_Y");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_SetRotate_Z(byte[] quaternion, float angle) {
        throw new UnsupportedToolboxException("Q3Quaternion_SetRotate_Z");
    }

    /** used by Vector2D */
    public static int Q3Vector2D_Add(byte[] v1, byte[] v2, byte[] result) {
        throw new UnsupportedToolboxException("Q3Vector2D_Add");
    }

    /** used by Vector2D */
    public static float Q3Vector2D_Cross(byte[] v1, byte[] v2) {
        throw new UnsupportedToolboxException("Q3Vector2D_Cross");
    }

    /** used by Vector2D */
    public static float Q3Vector2D_Dot(byte[] v1, byte[] v2) {
        throw new UnsupportedToolboxException("Q3Vector2D_Dot");
    }

    /** used by Vector2D */
    public static float Q3Vector2D_Length(byte[] vector2D) {
        throw new UnsupportedToolboxException("Q3Vector2D_Length");
    }

    /** used by Vector2D */
    public static int Q3Vector2D_Normalize(byte[] vector2D, byte[] result) {
        throw new UnsupportedToolboxException("Q3Vector2D_Normalize");
    }

    /** used by Vector2D */
    public static int Q3Vector2D_Scale(byte[] vector2D, float scalar, byte[] result) {
        throw new UnsupportedToolboxException("Q3Vector2D_Scale");
    }

    /** used by Vector2D */
    public static int Q3Vector2D_Subtract(byte[] v1, byte[] v2, byte[] result) {
        throw new UnsupportedToolboxException("Q3Vector2D_Subtract");
    }

    /** used by Vector3D */
    public static int Q3Vector3D_Add(byte[] v1, byte[] v2, byte[] result) {
        throw new UnsupportedToolboxException("Q3Vector3D_Add");
    }

    /** used by Vector3D */
    public static int Q3Vector3D_Cross(byte[] v1, byte[] v2, byte[] result) {
        throw new UnsupportedToolboxException("Q3Vector3D_Cross");
    }

    /** used by Vector3D */
    public static float Q3Vector3D_Dot(byte[] v1, byte[] v2) {
        throw new UnsupportedToolboxException("Q3Vector3D_Dot");
    }

    /** used by Vector3D */
    public static float Q3Vector3D_Length(byte[] vector3D) {
        throw new UnsupportedToolboxException("Q3Vector3D_Length");
    }

    /** used by Vector3D */
    public static int Q3Vector3D_Normalize(byte[] vector3D, byte[] result) {
        throw new UnsupportedToolboxException("Q3Vector3D_Normalize");
    }

    /** used by Vector3D */
    public static int Q3Vector3D_Scale(byte[] vector3D, float scalar, byte[] result) {
        throw new UnsupportedToolboxException("Q3Vector3D_Scale");
    }

    /** used by Vector3D */
    public static int Q3Vector3D_Subtract(byte[] v1, byte[] v2, byte[] result) {
        throw new UnsupportedToolboxException("Q3Vector3D_Subtract");
    }

    /** used by Vector3D */
    public static int Q3Vector3D_TransformQuaternion(byte[] vector3D, byte[] quaternion, byte[] result) {
        throw new UnsupportedToolboxException("Q3Vector3D_TransformQuaternion");
    }

}
