import React, { FC, ReactNode } from "react";
import Sidebar from "../components/Sidebar/Sidebar";
import Header from "../containers/Header/Header";
import { Props } from "../package/Model";

export const UserLayout:FC = ({ children }: Props) => {
  return (
    <>
      <div className="relative md:ml-64 bg-blueGray-100 h-screen">
        <Sidebar />
        <Header />
        <div className="w-full">
          {children}
          {/* <FooterAdmin /> */}
        </div>
      </div>
    </>
  );
};