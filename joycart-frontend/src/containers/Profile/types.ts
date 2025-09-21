
//用户资料响应类型
export type ResponseType = {
    success: boolean;
    message: string;
    data: {
        id: string;
        nickname: string;
        avatar: string;
        vipLevel: string;
        coupons: number;
        rewardPoints: number;
        phoneNumber: string;
        email: string;
    };
}