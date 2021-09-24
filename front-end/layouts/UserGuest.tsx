import Head from "next/head";
import React, { FC, ReactNode } from "react";

interface Props {
  children?: ReactNode;
}

const UserGuest: FC = ({ children }: Props) => {
  return (
    <div className="bg-background-login w-full h-screen bg-cover relative">
      <div className="bg-black opacity-50 w-full h-full absolute top-0 left-0 z-1"></div>
      {children}
    </div>
  );
};

export default UserGuest;